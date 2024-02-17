package space.atnibam.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.sms.mapper.CouponsMapper;
import space.atnibam.sms.model.dto.UserCouponDetailDTO;
import space.atnibam.sms.model.entity.Coupons;
import space.atnibam.sms.service.CouponsService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【coupons(优惠券表)】的数据库操作Service实现
 * @createDate 2024-02-17 22:52:54
 */
@Service
public class CouponsServiceImpl extends ServiceImpl<CouponsMapper, Coupons>
        implements CouponsService {

    @Resource
    private CouponsMapper couponsMapper;

    /**
     * 获取用户未使用且未过期的优惠券
     *
     * @param appId  应用ID
     * @param userId 用户ID
     * @return 用户未使用且未过期的优惠券列表
     */
    @Override
    public List<UserCouponDetailDTO> getUserUnexpiredCoupons(int appId, int userId) {
        return couponsMapper.getUserUnexpiredCoupons(appId, userId);
    }
}