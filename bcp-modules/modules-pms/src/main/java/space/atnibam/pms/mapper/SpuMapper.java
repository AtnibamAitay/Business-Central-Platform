package space.atnibam.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import space.atnibam.pms.model.entity.Spu;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【spu(商品表)】的数据库操作Mapper
 * @createDate 2024-02-08 21:44:45
 * @Entity space.atnibam.pms.model.entity.Spu
 */
public interface SpuMapper extends BaseMapper<Spu> {
    /**
     * 根据一个或多个商品ID获取商品基本的信息列表
     *
     * @param spuIdList 商品ID列表
     * @return 商品基本的信息列表
     */
    @Select("<script>SELECT spu_id, name, merchant_id FROM spu WHERE spu_id IN " +
            "<foreach item='item' index='index' collection='spuIdList' open='(' separator=',' close=')'>#{item}</foreach></script>")
    List<Spu> selectSpuBaseInfoListBySpuIds(List<Integer> spuIdList);
}