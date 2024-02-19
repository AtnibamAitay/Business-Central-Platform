package space.atnibam.api.ims;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.atnibam.common.core.domain.R;

/**
 * @InterfaceName: RemoteFriendService
 * @Description: 远程好友服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-19 09:23
 **/
@FeignClient(value = "modules-ims", contextId = "friend")
public interface RemoteFriendService {
    /**
     * 根据用户id获取收到的好友请求列表
     *
     * @param userId 用户id
     * @return 响应结果
     */
    @GetMapping("/api/friend-request/received/{userId}")
    R getReceivedFriendRequests(@PathVariable("userId") Integer userId);

    /**
     * 根据用户ID获取好友列表
     *
     * @param ownId 用户ID
     * @return 好友列表
     */
    @GetMapping("/api/friend-request/{ownId}")
    R getFriendList(@PathVariable int ownId);
}