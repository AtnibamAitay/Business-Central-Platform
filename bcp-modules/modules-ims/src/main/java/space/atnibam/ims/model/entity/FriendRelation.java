package space.atnibam.ims.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 好友表
 *
 * @TableName friend_relation
 */
@TableName(value = "friend_relation")
@Data
public class FriendRelation implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 关系id
     */
    @TableId(type = IdType.AUTO)
    private Integer relationId;
    /**
     * 好友id
     */
    private Integer friendId;
    /**
     * 自己的id
     */
    private Integer ownId;
    /**
     * 备注用户名
     */
    private String alias;
    /**
     * 关系（0代表黑名单好友、1代表正常好友）
     */
    private Integer relationship;
}