package space.atnibam.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.pms.model.dto.SpuDTO;
import space.atnibam.pms.model.entity.Spu;

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
}