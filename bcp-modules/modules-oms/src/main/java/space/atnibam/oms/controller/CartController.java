package space.atnibam.oms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.atnibam.api.oms.model.dto.ShoppingCartDTO;
import space.atnibam.oms.service.ShoppingCartService;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "购物车模块")
@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 根据用户Id和APPID查询用户的购物车
     *
     * @param userId 用户id
     * @param appId  应用ID
     * @return 购物车列表
     */
    @ApiOperation("根据用户id和APPID查询购物车")
    @GetMapping("")
    public List<ShoppingCartDTO> getShoppingCartByUserId(Integer userId, Integer appId) {
        return shoppingCartService.getShoppingCartByUserId(userId, appId);
    }
}