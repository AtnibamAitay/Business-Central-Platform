package space.atnibam.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import space.atnibam.sms.model.dto.UserCouponDetailDTO;
import space.atnibam.sms.model.entity.CouponMinSpendThresholds;

import java.math.BigDecimal;

/**
 * @author Atnibam Aitay
 * @description 针对表【coupon_min_spend_thresholds(满减券门槛表)】的数据库操作Mapper
 * @createDate 2024-02-17 22:52:54
 * @Entity space.atnibam.sms.model.entity.CouponMinSpendThresholds
 */
public interface CouponMinSpendThresholdsMapper extends BaseMapper<CouponMinSpendThresholds> {
    /**
     * 根据优惠券ID查询最低消费门槛和折扣金额
     *
     * @param couponId 优惠券ID
     * @return 最低消费门槛和折扣金额
     */
    @Select("SELECT min_order_amount, discount_amount FROM coupon_min_spend_thresholds WHERE coupon_id = #{couponId}")
    @Results({
            @Result(property = "minOrderAmount", column = "min_order_amount", javaType = BigDecimal.class),
            @Result(property = "discountAmount", column = "discount_amount", javaType = BigDecimal.class)
    })
    UserCouponDetailDTO.MinSpendThresholdsDTO getMinSpendThresholdsById(Integer couponId);
}