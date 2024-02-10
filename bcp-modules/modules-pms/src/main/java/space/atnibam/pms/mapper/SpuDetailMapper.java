package space.atnibam.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import space.atnibam.pms.model.entity.SpuDetail;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu_detail(SPU详细表)】的数据库操作Mapper
 * @createDate 2024-02-08 21:44:45
 * @Entity space.atnibam.pms.model.entity.SpuDetail
 */
public interface SpuDetailMapper extends BaseMapper<SpuDetail> {
    /**
     * 根据商品id获取spu介绍图列表
     *
     * @param spuId 商品id
     * @return spu介绍图列表
     */
    @Select("SELECT detail_url FROM spu_detail WHERE spu_id = #{spuId} ORDER BY display_order ASC")
    List<String> selectDetailUrlsBySpuId(Integer spuId);
}