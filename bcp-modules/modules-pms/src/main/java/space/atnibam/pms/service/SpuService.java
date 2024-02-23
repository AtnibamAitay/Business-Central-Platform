package space.atnibam.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.pms.model.dto.SpuBaseInfoDTO;
import space.atnibam.pms.model.dto.SpuDTO;
import space.atnibam.pms.model.entity.Spu;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName: SpuService
 * @Description: 商品服务接口
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-08 21:44
 **/
public interface SpuService extends IService<Spu> {
    /**
     * 根据商品ID查询商品信息
     *
     * @param spuId 商品ID
     * @return 商品实体对象，如果不存在则返回空Optional
     */
    Optional<SpuDTO> getSpuById(Integer spuId);

    /**
     * 根据一个或多个商品ID获取商品基本的信息列表
     *
     * @param spuIdList 商品ID列表
     * @return 商品基本的信息列表
     */
    List<SpuBaseInfoDTO> getSpuBaseInfoList(List<Integer> spuIdList);
}