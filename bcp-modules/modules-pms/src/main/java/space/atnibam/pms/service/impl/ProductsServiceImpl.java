package space.atnibam.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.pms.mapper.ProductsMapper;
import space.atnibam.pms.model.entity.Products;
import space.atnibam.pms.service.ProductsService;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @ClassName: ProductssServiceImpl
 * @Description: 针对表【productss(商品表)】的数据库操作Service实现
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-29 22:44
 **/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
        implements ProductsService {
    @Resource
    private ProductsMapper productsMapper;

    /**
     * 根据商品ID查询商品信息
     *
     * @param productId 商品ID
     * @return 商品实体对象，如果不存在则返回空Optional
     */
    @Override
    public Optional<Products> getProductById(Integer productId) {
        Products product = productsMapper.selectById(productId);
        return Optional.ofNullable(product);
    }

}




