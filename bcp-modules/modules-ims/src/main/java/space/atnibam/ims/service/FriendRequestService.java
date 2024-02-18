package space.atnibam.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.ims.model.dto.FriendRequestDTO;
import space.atnibam.ims.model.entity.FriendRequest;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_request(增添好友请求表)】的数据库操作Service
 * @createDate 2024-02-18 17:48:12
 */
public interface FriendRequestService extends IService<FriendRequest> {
    /**
     * 根据用户id获取收到的好友请求列表
     *
     * @param userId 用户id
     * @return 收到的好友请求列表
     */
    List<FriendRequestDTO> getReceivedFriendRequests(Integer userId);
}
