package space.atnibam.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.atnibam.pms.model.entity.SpuDetail;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu_detail(SPU详细表)】的数据库操作Service
 * @createDate 2024-02-08 21:44:45
 */
public interface SpuDetailService extends IService<SpuDetail> {

    /**
     * 根据商品id获取spu介绍图列表
     *
     * @param spuId 商品id
     * @return spu介绍图列表
     */
    List<String> getSpuDetailListBySpuId(Integer spuId);
}