package space.atnibam.ims.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.atnibam.common.core.domain.R;
import space.atnibam.ims.model.dto.FriendRequestDTO;
import space.atnibam.ims.model.dto.UserBaseInfoDTO;
import space.atnibam.ims.service.FriendRelationService;
import space.atnibam.ims.service.FriendRequestService;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "好友模块")
@RestController
@RequestMapping("/api/friend-request")
public class FriendController {

    @Resource
    private FriendRequestService friendRequestService;
    @Resource
    private FriendRelationService friendRelationService;

    /**
     * 根据用户id获取收到的好友请求列表
     *
     * @param userId 用户id
     * @return 响应结果
     */
    @ApiOperation("根据用户id获取收到的好友请求列表")
    @GetMapping("/received/{userId}")
    public R getReceivedFriendRequests(@PathVariable("userId") Integer userId) {
        List<FriendRequestDTO> requestList = friendRequestService.getReceivedFriendRequests(userId);
        return R.ok(requestList);
    }

    /**
     * 根据用户ID获取好友列表
     *
     * @param ownId 用户ID
     * @return 好友列表
     */
    @ApiOperation("根据用户ID获取好友列表")
    @GetMapping("/{ownId}")
    public R getFriendList(@PathVariable int ownId) {
        List<UserBaseInfoDTO> friendList = friendRelationService.getFriendListByUserId(ownId);
        return R.ok(friendList);
    }

}