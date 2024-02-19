package space.atnibam.ums.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 增添好友请求表
 *
 * @TableName friend_request
 */
@TableName(value = "friend_request")
@Data
public class FriendRequest implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 请求ID
     */
    @TableId(type = IdType.AUTO)
    private Integer requestId;
    /**
     * 请求增添的用户ID
     */
    private Integer userIdAdding;
    /**
     * 请求被增添的用户ID
     */
    private Integer userIdAdded;
    /**
     * 附言
     */
    private String message;
    /**
     * 添加状态码（0代表未处理、1代表已拒绝）
     */
    private Integer statusCode;
}