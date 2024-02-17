package space.atnibam.sms.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.atnibam.common.core.domain.R;
import space.atnibam.sms.service.CouponsService;

import javax.annotation.Resource;

/**
 * @ClassName: CouponController
 * @Description: 优惠券控制器
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-17 22:55
 **/
@Api(tags = "优惠券模块")
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Resource
    private CouponsService couponsService;

    /**
     * 获取用户未使用且未过期的优惠券
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 用户未使用且未过期的优惠券
     */
    @GetMapping("/user/coupons")
    public R getUserUnexpiredCoupons(
            @RequestParam(value = "appId") int appId,
            @RequestParam(value = "userId") int userId
    ) {
        return R.ok(couponsService.getUserUnexpiredCoupons(appId, userId));
    }
}