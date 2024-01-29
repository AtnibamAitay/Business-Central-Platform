package space.atnibam.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.pms.model.entity.Products;

import java.util.Optional;

/**
 * @ClassName: ProductsService
 * @Description: 针对表【products(商品表)】的数据库操作Service
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-29 22:44
 **/
public interface ProductsService extends IService<Products> {
    /**
     * 根据商品ID查询商品信息
     *
     * @param productId 商品ID
     * @return 商品实体对象，如果不存在则返回空Optional
     */
    Optional<Products> getProductById(Integer productId);
}
