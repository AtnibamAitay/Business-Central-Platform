package space.atnibam.cms.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.cms.constant.CommonCacheContant;
import space.atnibam.cms.mapper.CommentMapper;
import space.atnibam.cms.model.dto.CommentDTO;
import space.atnibam.cms.model.dto.CommentNodeDTO;
import space.atnibam.cms.model.entity.Comment;
import space.atnibam.cms.service.CommentService;
import space.atnibam.common.core.domain.R;
import space.atnibam.common.redis.constant.CacheConstants;
import space.atnibam.common.redis.utils.CacheClient;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static space.atnibam.cms.constant.CommonCacheContant.COMMENT_CACHE_PREFIX;
import static space.atnibam.common.redis.constant.CacheConstants.REDIS_SEPARATOR;

/**
 * @ClassName: CommentServiceImpl
 * @Description: 评论相关业务的具体实现
 * @Author: AtnibamAitay
 * @CreateTime: 2023-10-19 21:00
 **/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    private final StringRedisTemplate stringRedisTemplate;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private RemoteUserInfoService remoteUserInfoService;
    @Resource
    private ObjectMapper objectMapper;

    public CommentServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 根据对象ID获取顶级评论
     *
     * @param objectId 对象的ID
     * @param pageNum  页码
     * @param pageSize 每页的数量
     * @return R 评论结果
     */
    @Override
    public R getTopLevelCommentsByObjectId(Integer objectId, Integer pageNum, Integer pageSize, String objectType) {
        // 获取objectId对应的所有根评论
        List<CommentDTO> rootCommentList = getRootCommentByObjectId(objectId, pageNum, pageSize, objectType);
        // TODO:加入缓存
        // 创建新的方法，传入false以表示不包含子评论
        return R.ok(rootCommentList);
    }

    /**
     * 根据对象ID获取嵌套的评论
     * 使用了互斥锁防止缓存击穿的思路
     *
     * @param objectId 对象的ID
     * @param pageNum  页码
     * @param pageSize 每页的数量
     * @return R 评论结果
     */
    @Override
    public R getNestedCommentsByObjectId(Integer objectId, Integer pageNum, Integer pageSize, String objectType) {
        // 用于存储评论的列表
        List<CommentNodeDTO> commentNodeDTOList;

        // 构造缓存键
        // TODO:这里需要改成一个更合理的缓存键
        String cacheKey = COMMENT_CACHE_PREFIX + pageNum + REDIS_SEPARATOR + pageSize + REDIS_SEPARATOR + objectId;

        // 尝试从缓存中查询评论
        String cacheJson = stringRedisTemplate.opsForValue().get(cacheKey);

        // 缓存中存在评论
        if (StrUtil.isNotBlank(cacheJson)) {
            ObjectMapper mapper = new ObjectMapper();
            // 使用ObjectMapper反序列化JSONArray为R类型的List
            List<CommentNodeDTO> result;
            try {
                result = mapper.readValue(cacheJson, mapper.getTypeFactory().constructCollectionType(List.class, CommentNodeDTO.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return R.ok(result);
        }

        // 判断是否命中空值
        if (cacheJson != null) {
            return null;
        }

        // 构建互斥锁键
        // TODO:这里需要改成一个更合理的互斥锁键
        String lockKey = CacheConstants.LOCK_KEY + objectId + pageNum + pageSize;

        while (true) {
            try {
                // 尝试获取互斥锁，防止缓存击穿
                boolean isLock = cacheClient.tryLock(lockKey, CommonCacheContant.COMMENT_REBUILD_LOCK_TTL, TimeUnit.SECONDS);

                // 成功获取锁
                if (isLock) {
                    // 从数据库中获取评论
                    commentNodeDTOList = buildCommentTree(objectId, pageNum, pageSize, objectType);

                    // 增添到缓存
                    cacheClient.set(cacheKey, commentNodeDTOList,
                            CommonCacheContant.COMMENT_CACHE_TTL, TimeUnit.HOURS);

                    // 成功获取数据后跳出循环
                    break;
                } else {
                    // 获取锁失败，休眠一段时间后继续尝试
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                log.error("尝试获取锁时发生中断异常", e);
            } finally {
                // 无论是否获取成功，最后都需要释放锁
                cacheClient.unlock(lockKey);
            }
        }

        return R.ok(commentNodeDTOList);
    }

    /**
     * 根据对象ID获取评论的平均评分
     *
     * @param objectId 对象的ID
     * @return R 评论结果
     */
    @Override
    public R getAverageGradeByObjectId(Integer objectId, String objectType) {
        Double averageGrade = commentMapper.selectAverageGradeByObjectId(objectId, objectType);
        // 保留两位小数
        averageGrade = Double.valueOf(NumberFormat.getNumberInstance(Locale.getDefault()).format(averageGrade));
        // 或者使用 BigDecimal 进行精确计算和格式化
        averageGrade = BigDecimal.valueOf(averageGrade).setScale(2, RoundingMode.HALF_UP).doubleValue();

        if (averageGrade == null) {
            // TODO:一个默认分，待考虑默认综合分应该设置为多少
            averageGrade = 5.0;
        }
        return R.ok(averageGrade);
    }

    /**
     * 根据objectId、pageNum和pageSize构建评论树
     *
     * @param objectId 对象ID
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @return 评论节点列表
     */
    private List<CommentNodeDTO> buildCommentTree(Integer objectId, Integer pageNum, Integer pageSize, String objectType) {

        // 获取objectId对应的所有根评论
        List<CommentDTO> rootCommentList = getRootCommentByObjectId(objectId, pageNum, pageSize, objectType);

        // 初始化一个ArrayList，用于存储根评论的转化结果（由Comment类型转化为CommentNodeDTO类型）
        List<CommentNodeDTO> rootComments = new ArrayList<>();
        for (CommentDTO comment : rootCommentList) {
            rootComments.add(new CommentNodeDTO(comment));
        }

        // 初始化一个HashMap ，用于存储每个根评论的子评论，其中key为父评论ID，value为该父评论下的所有子评论
        Map<Integer, List<CommentNodeDTO>> groupedSubComments = new HashMap<>();

        // 遍历根评论，并获取每个根评论的所有子评论
        for (CommentNodeDTO rootComment : rootComments) {

            // 使用父评论ID去查询子评论
            List<CommentDTO> subCommentList = getSubCommentByParentId(rootComment.getComment().getId());

            // 遍历子评论，并将其添加到groupedSubComments中
            for (CommentDTO comment : subCommentList) {
                groupedSubComments.computeIfAbsent(comment.getObjectId(), k -> new ArrayList<>())
                        .add(new CommentNodeDTO(comment));
            }

            // 对每个根评论进行深度优先搜索，找出其所有子评论，并将子评论添加到对应的根评论下
            dfs(rootComment, groupedSubComments);
        }

        // 返回包含所有根评论及其子评论的列表
        return rootComments;
    }

    /**
     * 根据objectId获取所有根评论
     *
     * @param objectId 对象ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 根评论列表
     */
    private List<CommentDTO> getRootCommentByObjectId(Integer objectId, Integer pageNum, Integer pageSize, String objectType) {
        pageNum = (pageNum - 1) * pageSize;
        List<Comment> rootComments = commentMapper.selectRootCommentByObjectId(objectId, pageNum, pageSize, objectType);

        return rootComments.stream()
                .map(this::mapCommentToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据父评论ID获取所有子评论，如果子评论下还有子评论，也一并获取
     *
     * @param parentId 父评论ID
     * @return 所有的子评论列表
     */
    private List<CommentDTO> getSubCommentByParentId(Integer parentId) {

        // 创建一个结果集合用于存储所有查到的子评论
        List<CommentDTO> allSubComments = new ArrayList<>();

        // 使用parentId查找子评论
        List<Comment> subComments = commentMapper.selectSubCommentByParentId(parentId);

        List<CommentDTO> commentDTOList = subComments.stream()
                .map(this::mapCommentToDTO)
                .collect(Collectors.toList());

        // 如果查找到了子评论，则继续查找其子评论
        if (!subComments.isEmpty()) {

            // 将查找到的子评论添加到结果集合中
            allSubComments.addAll(commentDTOList);

            // 对每个子评论进行递归查找其子评论
            for (Comment comment : subComments) {
                // 从当前子评论开始，递归地获取其所有子评论
                List<CommentDTO> childSubComments = getSubCommentByParentId(comment.getId());
                // 将递归获取的子评论添加到结果集合中
                allSubComments.addAll(childSubComments);
            }
        }

        // 返回所有查找到的子评论
        return allSubComments;
    }

    /**
     * 通过深度优先搜索（DFS）遍历并构建评论树。首先，我们在所有的子评论中通过父节点ID进行分组，
     * 这样就可以快速查找到给定节点的所有子节点。然后，对于每个节点，我们查找它的所有子节点，
     * 并进一步为每个子节点执行相同的操作。这是一个递归流程。
     *
     * @param current         当前节点
     * @param groupedComments 基于父节点ID分组的评论节点
     */
    private void dfs(CommentNodeDTO current, Map<Integer, List<CommentNodeDTO>> groupedComments) {
        // 从groupedComments中获取当前节点的所有子节点
        List<CommentNodeDTO> children = groupedComments.get(current.getComment().getId());

        // 如果当前节点有子节点则进入if语句块
        if (children != null) {
            // 遍历当前节点的所有子节点
            for (CommentNodeDTO child : children) {
                // 将子节点添加到当前节点的子节点列表中
                current.getSubComment().add(child);
                // 递归处理该子节点，即对该子节点再次执行dfs方法，查找其子节点
                dfs(child, groupedComments);
            }
        }

    }

    /**
     * 将Comment对象转换为CommentDTO对象
     *
     * @param comment Comment对象
     * @return CommentDTO对象
     */
    private CommentDTO mapCommentToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        // 查询用户信息
        Object commentUserInfo = remoteUserInfoService.getDetailedUserInfo(comment.getUserId()).getData();
        // 将用户信息转换为CommentUserInfoDTO对象
        Map<String, Object> merchantDataMap = (Map<String, Object>) commentUserInfo;
        CommentDTO.CommentUserInfoDTO commentUserInfoDTO = objectMapper.convertValue(merchantDataMap, CommentDTO.CommentUserInfoDTO.class);
        commentDTO.setCommentUserInfo(commentUserInfoDTO);

        return commentDTO;
    }


}