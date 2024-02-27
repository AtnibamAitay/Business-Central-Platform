package space.atnibam.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import space.atnibam.api.oms.model.dto.ShoppingCartDTO;
import space.atnibam.api.pms.RemoteSpuService;
import space.atnibam.api.pms.model.dto.SpuDTO;
import space.atnibam.oms.mapper.ShoppingCartMapper;
import space.atnibam.oms.model.entity.ShoppingCart;
import space.atnibam.oms.service.ShoppingCartService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【shopping_cart】的数据库操作Service实现
 * @createDate 2024-02-23 17:06:41
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private RemoteSpuService remoteSpuService;

    /**
     * 获取用户的购物车列表
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 购物车列表
     */
    @Override
    public List<ShoppingCartDTO> getShoppingCartByUserId(Integer userId, Integer appId) {
        List<ShoppingCartDTO> shoppingCartDTOList = new ArrayList<>();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserId(userId);
        for (ShoppingCart shoppingCart : shoppingCarts) {
            ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
            // 获取商品详情
            Object spuDetail = remoteSpuService.getSpuDetail(shoppingCart.getSpuId()).getData();
            // 将商品详情转换为SpuDTO对象
            SpuDTO spuDTO = objectMapper.convertValue(spuDetail, SpuDTO.class);
            // 复制SpuDTO对象的属性到ShoppingCartDTO对象
            BeanUtils.copyProperties(spuDTO, shoppingCartDTO);
            // 如果商品详情中包含封面图片，则将第一个封面图片设置为购物车的封面图片
            if (spuDTO.getCover().size() != 0) {
                shoppingCartDTO.setCoverImage(spuDTO.getCover().get(0));
            }
            // 设置购物车数量
            shoppingCartDTO.setQuantity(shoppingCart.getQuantity());
            // TODO: 价格和库存两个字段的数据还没有
            // 将ShoppingCartDTO对象添加到购物车列表中
            shoppingCartDTOList.add(shoppingCartDTO);
        }
        return shoppingCartDTOList;
    }

}