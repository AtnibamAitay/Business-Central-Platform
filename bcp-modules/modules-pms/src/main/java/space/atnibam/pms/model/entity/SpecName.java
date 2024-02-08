package space.atnibam.pms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 规格名表
 *
 * @TableName spec_name
 */
@TableName(value = "spec_name")
@Data
public class SpecName implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 规格名id
     */
    @TableId(type = IdType.AUTO)
    private Integer specNameId;
    /**
     * 商品id
     */
    private Integer spuId;
    /**
     * 规格名
     */
    private String specName;
    /**
     * 显示顺序
     */
    private Integer displayOrder;
}