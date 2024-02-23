package space.atnibam.oms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName shopping_cart
 */
@TableName(value = "shopping_cart")
@Data
public class ShoppingCart implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 购物车主键
     */
    @TableId(type = IdType.AUTO)
    private Integer cartId;
    /**
     * SPU ID
     */
    private Integer spuId;
    /**
     * SKU ID
     */
    private Integer skuId;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 加入购物车时的价格
     */
    private BigDecimal priceAtAddition;
}