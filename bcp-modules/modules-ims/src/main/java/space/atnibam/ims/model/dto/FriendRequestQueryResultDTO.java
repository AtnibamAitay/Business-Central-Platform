package space.atnibam.ims.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendRequestQueryResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    private Integer requestId;
    
    /**
     * 请求增添的用户ID
     */
    private Integer requesterId;

    /**
     * 发送的好友请求附带的消息内容
     */
    private String message;

    /**
     * 添加好友的状态码，用于标识请求处理的状态（0代表未处理、1代表已拒绝）
     */
    private Integer statusCode;
}
