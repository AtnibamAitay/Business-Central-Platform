package space.atnibam.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import space.atnibam.pms.model.entity.SpuCover;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu_cover(SPU封面表)】的数据库操作Mapper
 * @createDate 2024-02-08 21:44:45
 * @Entity space.atnibam.pms.model.entity.SpuCover
 */
public interface SpuCoverMapper extends BaseMapper<SpuCover> {
    /**
     * 根据商品id获取spu封面列表
     *
     * @param spuId 商品id
     * @return spu封面列表
     */
    @Select("SELECT cover_url FROM spu_cover WHERE spu_id = #{spuId} ORDER BY display_order ASC")
    List<String> selectCoverUrlsBySpuId(Integer spuId);
}
