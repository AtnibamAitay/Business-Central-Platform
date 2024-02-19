package space.atnibam.ims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.ims.mapper.FriendRelationMapper;
import space.atnibam.ims.model.dto.UserBaseInfoDTO;
import space.atnibam.ims.model.entity.FriendRelation;
import space.atnibam.ims.service.FriendRelationService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Atnibam Aitay
 * @description 针对表【friend_relation(好友表)】的数据库操作Service实现
 * @createDate 2024-02-18 17:48:12
 */
@Service
public class FriendRelationServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelation>
        implements FriendRelationService {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private FriendRelationMapper friendRelationMapper;
    @Resource
    private RemoteUserInfoService remoteUserInfoService;

    /**
     * 根据用户ID获取该用户的好友列表
     *
     * @param ownId 用户ID
     * @return 好友列表
     */
    @Override
    public List<UserBaseInfoDTO> getFriendListByUserId(int ownId) {
        // TODO:暂时不考虑备注名的情况
        List<Integer> friendRelationsByOwnId = friendRelationMapper.getFriendRelationsByOwnId(ownId);
        Object basicUserInfo = remoteUserInfoService.getBasicUserInfo(friendRelationsByOwnId).getData();
        List<Map<String, Object>> basicUserInfoDataMap = (List<Map<String, Object>>) basicUserInfo;
        List<UserBaseInfoDTO> userBaseInfoDTOList = new ArrayList<>();
        for (Map<String, Object> basicUserInfoData : basicUserInfoDataMap) {
            UserBaseInfoDTO userBaseInfoDTO = objectMapper.convertValue(basicUserInfoData, UserBaseInfoDTO.class);
            userBaseInfoDTOList.add(userBaseInfoDTO);
        }
        return userBaseInfoDTOList;
    }
}