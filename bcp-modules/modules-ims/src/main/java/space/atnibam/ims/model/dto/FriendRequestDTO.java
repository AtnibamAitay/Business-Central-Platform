package space.atnibam.ims.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FriendRequestDTO
 * @Description: 该类表示好友请求的数据传输对象，包含请求者信息、附言以及添加状态码等数据
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-18 19:02
 */
@Data
public class FriendRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    private Integer requestId;
    /**
     * 请求增添的用户信息
     */
    private UserBaseInfoDTO requesterInfo;
    /**
     * 发送的好友请求附带的消息内容
     */
    private String message;
    /**
     * 添加好友的状态码，用于标识请求处理的状态
     */
    private Integer statusCode;
}
