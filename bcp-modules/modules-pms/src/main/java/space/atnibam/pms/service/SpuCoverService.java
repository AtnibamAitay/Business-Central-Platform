package space.atnibam.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.pms.model.entity.SpuCover;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu_cover(SPU封面表)】的数据库操作Service
 * @createDate 2024-02-08 21:44:45
 */
public interface SpuCoverService extends IService<SpuCover> {
    /**
     * 根据商品id获取spu封面信息
     *
     * @param spuId 商品id
     * @return Optional<SpuCover> spu封面
     */
    List<String> getSpuCoverListBySpuId(Integer spuId);
}
