package space.atnibam.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券表
 *
 * @TableName coupons
 */
@TableName(value = "coupons")
@Data
public class Coupons implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 优惠券ID
     */
    @TableId(type = IdType.AUTO)
    private Integer couponId;
    /**
     * 优惠券名
     */
    private String couponName;
    /**
     * 优惠券兑换码
     */
    private String redeemCode;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 优惠券类型（1满减券、2直减券、3折扣券）
     */
    private Integer couponType;
    /**
     * 发券人ID
     */
    private Integer issuerId;
    /**
     * 发行量（0代表无限制）
     */
    private Integer issueQuantity;
    /**
     * APPID
     */
    private Integer appId;
    /**
     * 状态（0已删除、1正常）
     */
    private Integer status;
}