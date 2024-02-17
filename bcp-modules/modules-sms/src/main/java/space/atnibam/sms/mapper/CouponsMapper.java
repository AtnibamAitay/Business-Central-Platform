package space.atnibam.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import space.atnibam.sms.model.dto.UserCouponDetailDTO;
import space.atnibam.sms.model.entity.Coupons;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【coupons(优惠券表)】的数据库操作Mapper
 * @createDate 2024-02-17 22:52:54
 * @Entity space.atnibam.sms.model.entity.Coupons
 */
public interface CouponsMapper extends BaseMapper<Coupons> {
    /**
     * 根据用户ID和应用ID获取用户未过期的优惠券列表
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 未过期的优惠券列表
     */
    // FIXME:返回的列表中MinSpendThresholdsDTO无数据，需要解决
    @Select("""
                SELECT
                    c.coupon_id,
                    c.coupon_name,
                    c.start_date,
                    uc.expire_date,
                    cmst.min_order_amount,
                    cmst.discount_amount
                FROM user_coupons uc
                INNER JOIN coupons c ON uc.coupon_id = c.coupon_id
                LEFT JOIN coupon_min_spend_thresholds cmst ON c.coupon_id = cmst.coupon_id
                WHERE uc.user_id = #{userId}
                  AND uc.status = 1
                  AND c.app_id = #{appId}
                  AND c.status = 1
                  AND (
                      uc.expire_date IS NULL
                      OR uc.expire_date > NOW()
                  )
            """)
    List<UserCouponDetailDTO> getUserUnexpiredCoupons(int appId, int userId);
}