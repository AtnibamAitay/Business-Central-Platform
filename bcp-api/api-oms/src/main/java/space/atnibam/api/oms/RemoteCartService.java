package space.atnibam.api.oms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.atnibam.api.oms.model.dto.ShoppingCartDTO;

import java.util.List;

/**
 * @InterfaceName: RemoteCartService
 * @Description: 远程调用购物车服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-27 14:47
 **/
@FeignClient(value = "modules-oms", contextId = "cart")
public interface RemoteCartService {
    /**
     * 根据用户Id和APPID查询用户的购物车
     *
     * @param userId 用户id
     * @param appId  应用ID
     * @return 购物车列表
     */
    @GetMapping("/api/carts")
    List<ShoppingCartDTO> getShoppingCartByUserId(@RequestParam Integer userId, @RequestParam Integer appId);
}