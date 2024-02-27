package space.atnibam.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import space.atnibam.oms.model.entity.ShoppingCart;

import java.util.List;

/**
 * @author Atnibam Aitay
 * @description 针对表【shopping_cart】的数据库操作Mapper
 * @createDate 2024-02-23 17:06:41
 * @Entity space.atnibam.oms.model.entity.ShoppingCart
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    /**
     * 根据用户ID查询购物车信息
     *
     * @param userId 用户ID
     * @return ShoppingCart实体列表
     */
    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId}")
    List<ShoppingCart> selectByUserId(@Param("userId") int userId);
}