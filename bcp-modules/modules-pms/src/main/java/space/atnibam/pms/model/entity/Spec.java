package space.atnibam.pms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 规格表
 *
 * @TableName spec
 */
@TableName(value = "spec")
@Data
public class Spec implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 规格id
     */
    @TableId(type = IdType.AUTO)
    private Integer specId;
    /**
     * 商品id
     */
    private Integer spuId;
    /**
     * 规格
     */
    private Object specn;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 价格
     */
    private BigDecimal price;
}