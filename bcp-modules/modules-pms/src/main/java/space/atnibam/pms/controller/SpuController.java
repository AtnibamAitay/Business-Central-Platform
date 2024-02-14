package space.atnibam.pms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.atnibam.api.pms.RemoteSpuService;
import space.atnibam.common.core.domain.R;
import space.atnibam.pms.model.dto.SpuDTO;
import space.atnibam.pms.service.SpuService;

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
public class SpuController implements RemoteSpuService {
    @Resource
    private SpuService spuService;

    /**
     * 根据商品ID获取商品信息
     *
     * @param spuId 商品ID
     * @return 商品信息
     */
    @Override
    @ApiOperation("根据商品ID获取商品信息")
    @GetMapping("/{spuId}")
    public R getSpuDetail(@PathVariable("spuId") Integer spuId) {
        // 查询商品信息
        Optional<SpuDTO> optionalProduct = spuService.getSpuById(spuId);
        // 返回查询结果
        // TODO:更换为自定义异常
        return R.ok(optionalProduct.orElseThrow(() -> new RuntimeException("商品不存在")));
    }
}