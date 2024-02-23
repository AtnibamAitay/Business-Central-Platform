package space.atnibam.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.oms.mapper.ShoppingCartMapper;
import space.atnibam.oms.model.entity.ShoppingCart;
import space.atnibam.oms.service.ShoppingCartService;

/**
 * @author Atnibam Aitay
 * @description 针对表【shopping_cart】的数据库操作Service实现
 * @createDate 2024-02-23 17:06:41
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

}