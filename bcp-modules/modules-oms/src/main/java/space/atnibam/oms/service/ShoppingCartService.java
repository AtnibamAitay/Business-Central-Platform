package space.atnibam.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.api.oms.model.dto.ShoppingCartDTO;
import space.atnibam.oms.model.entity.ShoppingCart;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【shopping_cart】的数据库操作Service
 * @createDate 2024-02-23 17:06:41
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 获取用户的购物车列表
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 购物车列表
     */
    List<ShoppingCartDTO> getShoppingCartByUserId(Integer userId, Integer appId);
}