package space.atnibam.api.pms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.atnibam.api.pms.model.dto.SpuBaseInfoDTO;
import space.atnibam.common.core.domain.R;

import java.util.List;

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
    @GetMapping("/api/spu/{spuId}")
    R getSpuDetail(@PathVariable("spuId") Integer spuId);

    /**
     * 根据一个或多个商品ID获取商品基本的信息列表
     *
     * @param spuIdList 商品ID列表
     * @return 商品信息列表
     */
    @PostMapping("/api/spu/baseInfo/list")
    List<SpuBaseInfoDTO> getSpuDetailList(@RequestBody List<Integer> spuIdList);
}