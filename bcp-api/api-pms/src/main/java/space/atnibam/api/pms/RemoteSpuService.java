package space.atnibam.api.pms;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.atnibam.common.core.domain.R;

/**
 * @InterfaceName: RemoteSpuService
 * @Description: 远程调用商品服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-03 10:53
 **/
@FeignClient(value = "modules-pms", contextId = "spu")
public interface RemoteSpuService {

    /**
     * 根据商品ID获取商品信息
     *
     * @param spuId 商品ID
     * @return 商品信息
     */
    @ApiOperation("根据商品ID获取商品信息")
    @GetMapping("/api/spu/{spuId}")
    R getSpuDetail(@PathVariable("spuId") Integer spuId);

}