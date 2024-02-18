package space.atnibam.ims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.ims.mapper.FriendRequestMapper;
import space.atnibam.ims.model.dto.FriendRequestDTO;
import space.atnibam.ims.model.entity.FriendRequest;
import space.atnibam.ims.service.FriendRequestService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_request(增添好友请求表)】的数据库操作Service实现
 * @createDate 2024-02-18 17:48:12
 */
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest>
        implements FriendRequestService {

    @Resource
    private FriendRequestMapper friendRequestMapper;

    /**
     * 根据用户id获取收到的好友请求列表
     *
     * @param userId 用户id
     * @return 收到的好友请求列表
     */
    @Override
    public List<FriendRequestDTO> getReceivedFriendRequests(Integer userId) {
        // TODO:待实现
        return null;
    }
}




