package space.atnibam.pms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import space.atnibam.api.pms.RemoteSpuService;
import space.atnibam.api.pms.model.dto.SpuBaseInfoDTO;
import space.atnibam.api.pms.model.dto.SpuDTO;
import space.atnibam.common.core.domain.R;
import space.atnibam.pms.service.SpuService;

import javax.annotation.Resource;
import java.util.List;
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

    /**
     * 根据一个或多个商品ID获取商品基本的信息列表
     *
     * @param spuIdList 商品ID列表
     * @return 商品信息列表
     */
    @Override
    @ApiOperation("根据一个或多个商品ID获取商品基本的信息列表")
    @PostMapping("/baseInfo/list")
    public List<SpuBaseInfoDTO> getSpuDetailList(@RequestBody List<Integer> spuIdList) {
        return spuService.getSpuBaseInfoList(spuIdList);
    }
}