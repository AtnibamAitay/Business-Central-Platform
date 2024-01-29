package space.atnibam.pms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.atnibam.pms.model.entity.Products;
import space.atnibam.pms.service.ProductsService;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @ClassName: SpuController
 * @Description: 商品模块接口
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-29 22:55
 **/
@Api(tags = "商品模块")
@RestController
@RequestMapping("/api/spu")
public class SpuController {
    @Resource
    private ProductsService productsService;

    /**
     * 根据商品ID获取商品信息
     *
     * @param productId 商品ID
     * @return 商品信息
     */
    @ApiOperation("根据商品ID获取商品信息")
    @GetMapping("/{productId}")
    public Products getProduct(@PathVariable("productId") Integer productId) {
        // 调用service层方法查询商品
        Optional<Products> optionalProduct = productsService.getProductById(productId);
        // 返回查询结果
        // TODO:更换为自定义异常
        return optionalProduct.orElseThrow(() -> new RuntimeException("商品不存在"));
    }
}