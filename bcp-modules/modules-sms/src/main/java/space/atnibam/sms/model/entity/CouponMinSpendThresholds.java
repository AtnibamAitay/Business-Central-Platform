package space.atnibam.sms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 满减券门槛表
 *
 * @TableName coupon_min_spend_thresholds
 */
@TableName(value = "coupon_min_spend_thresholds")
@Data
public class CouponMinSpendThresholds implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 满减券门槛ID
     */
    @TableId(type = IdType.AUTO)
    private Integer thresholdId;
    /**
     * 优惠券ID
     */
    private Integer couponId;
    /**
     * 最低订单金额
     */
    private BigDecimal minOrderAmount;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
}