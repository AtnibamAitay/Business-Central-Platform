package space.atnibam.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.atnibam.pms.mapper.ProductsMapper;
import space.atnibam.pms.model.entity.Products;
import space.atnibam.pms.service.ProductsService;

/**
 * @ClassName: ProductsServiceImpl
 * @Description: 针对表【products(商品表)】的数据库操作Service实现
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-29 22:44
 **/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
        implements ProductsService {

}




