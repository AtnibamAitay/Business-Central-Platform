package space.atnibam.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.sms.model.dto.UserCouponDetailDTO;
import space.atnibam.sms.model.entity.Coupons;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【coupons(优惠券表)】的数据库操作Service
 * @createDate 2024-02-17 22:52:54
 */
public interface CouponsService extends IService<Coupons> {
    /**
     * 获取用户未使用且未过期的优惠券
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 用户未使用且未过期的优惠券列表
     */
    List<UserCouponDetailDTO> getUserUnexpiredCoupons(int appId, int userId);
}
