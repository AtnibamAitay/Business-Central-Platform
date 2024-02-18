package space.atnibam.api.sms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.atnibam.common.core.domain.R;

/**
 * @InterfaceName: RemoteCouponService
 * @Description: 远程调用优惠券服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-18 11:50
 **/
@FeignClient(value = "modules-sms", contextId = "coupon")
public interface RemoteCouponService {
    /**
     * 获取用户未使用且未过期的优惠券
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 用户未使用且未过期的优惠券
     */
    @GetMapping("/api/coupon/user/coupons")
    R getUserUnexpiredCoupons(
            @RequestParam(value = "appId") int appId,
            @RequestParam(value = "userId") int userId
    );
}