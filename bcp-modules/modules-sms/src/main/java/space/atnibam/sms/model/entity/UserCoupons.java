package space.atnibam.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户的优惠券表
 *
 * @TableName user_coupons
 */
@TableName(value = "user_coupons")
@Data
public class UserCoupons implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 优惠券id
     */
    @TableId(type = IdType.AUTO)
    private Integer couponId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 获得时间
     */
    private Date acquireTime;
    /**
     * 失效日期
     */
    private Date expireDate;
    /**
     * 使用情况（0代表已使用、1代表未使用、2代表已过期）
     */
    private Integer status;
    /**
     * 使用时间
     */
    private Date useTime;
}