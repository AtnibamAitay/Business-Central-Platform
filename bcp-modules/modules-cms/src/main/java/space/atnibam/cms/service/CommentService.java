package space.atnibam.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.cms.model.entity.Comment;
import space.atnibam.common.core.domain.R;

/**
 * @ClassName: CommentService
 * @Description: 针对表【comment】的数据库操作Service
 * @Author: AtnibamAitay
 * @CreateTime: 2023-10-19 21:00
 **/
public interface CommentService extends IService<Comment> {

    /**
     * 根据对象ID获取顶级评论
     *
     * @param objectId 对象的ID
     * @param pageNum  页码
     * @param pageSize 每页的数量
     * @return R 评论结果
     */
    R getTopLevelCommentsByObjectId(Integer objectId, Integer pageNum, Integer pageSize, String objectType);

    /**
     * 根据对象ID获取嵌套的评论
     *
     * @param objectId 对象的ID
     * @param pageNum  页码
     * @param pageSize 每页的数量
     * @return R 评论结果
     */
    R getNestedCommentsByObjectId(Integer objectId, Integer pageNum, Integer pageSize, String objectType);

}
