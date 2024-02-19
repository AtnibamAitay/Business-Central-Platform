package space.atnibam.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.ums.mapper.FriendRequestMapper;
import space.atnibam.ums.model.dto.FriendRequestDTO;
import space.atnibam.ums.model.dto.FriendRequestQueryResultDTO;
import space.atnibam.ums.model.dto.UserBaseInfoDTO;
import space.atnibam.ums.model.entity.FriendRequest;
import space.atnibam.ums.service.FriendRequestService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_request(增添好友请求表)】的数据库操作Service实现
 * @createDate 2024-02-18 17:48:12
 */
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest>
        implements FriendRequestService {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private FriendRequestMapper friendRequestMapper;
    @Resource
    private RemoteUserInfoService remoteUserInfoService;

    /**
     * 根据用户id获取收到的好友请求列表（核心逻辑调用其他辅助方法）
     *
     * @param userId 用户id
     * @return 收到的好友请求列表
     */
    @Override
    public List<FriendRequestDTO> getReceivedFriendRequests(Integer userId) {
        List<FriendRequestQueryResultDTO> dbFriendRequests = fetchFriendRequestsFromDB(userId);
        return convertToFriendRequestDTOs(dbFriendRequests);
    }

    /**
     * 从数据库中获取指定用户收到的好友请求
     *
     * @param userId 用户id
     * @return 好友请求查询结果列表
     */
    private List<FriendRequestQueryResultDTO> fetchFriendRequestsFromDB(Integer userId) {
        return friendRequestMapper.getFriendRequestsByUserId(userId);
    }

    /**
     * 将好友请求查询结果转换为FriendRequestDTO对象列表
     *
     * @param queryResultList 好友请求查询结果列表
     * @return 转换后的FriendRequestDTO对象列表
     */
    private List<FriendRequestDTO> convertToFriendRequestDTOs(List<FriendRequestQueryResultDTO> queryResultList) {
        List<FriendRequestDTO> friendRequests = new ArrayList<>();
        for (FriendRequestQueryResultDTO queryResult : queryResultList) {
            FriendRequestDTO friendRequestDTO = createFriendRequestDTO(queryResult);
            UserBaseInfoDTO userInfo = fetchUserInfo(queryResult.getRequesterId());
            friendRequestDTO.setRequesterInfo(userInfo);
            friendRequests.add(friendRequestDTO);
        }
        return friendRequests;
    }

    /**
     * 创建一个FriendRequestDTO实例并填充基础数据
     *
     * @param queryResult 查询结果
     * @return 填充基础数据后的FriendRequestDTO实例
     */
    private FriendRequestDTO createFriendRequestDTO(FriendRequestQueryResultDTO queryResult) {
        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        BeanUtils.copyProperties(queryResult, friendRequestDTO);
        return friendRequestDTO;
    }

    /**
     * 获取详细用户信息
     *
     * @param requesterId 发起请求的用户id
     * @return 用户基本信息DTO
     */
    private UserBaseInfoDTO fetchUserInfo(Integer requesterId) {
        Object userInfoData = remoteUserInfoService.getDetailedUserInfo(requesterId).getData();
        Map<String, Object> userInfoDataMap = (Map<String, Object>) userInfoData;
        return objectMapper.convertValue(userInfoDataMap, UserBaseInfoDTO.class);
    }
}