package space.atnibam.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.ums.model.dto.UserBaseInfoDTO;
import space.atnibam.ums.model.entity.FriendRelation;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_relation(好友表)】的数据库操作Service
 * @createDate 2024-02-18 17:48:12
 */
public interface FriendRelationService extends IService<FriendRelation> {
    /**
     * 根据用户ID获取该用户的好友列表
     *
     * @param ownId 用户ID
     * @return 好友列表
     */
    List<UserBaseInfoDTO> getFriendListByUserId(int ownId);
}
