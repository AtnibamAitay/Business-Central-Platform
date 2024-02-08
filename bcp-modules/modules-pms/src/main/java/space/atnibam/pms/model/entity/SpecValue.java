package space.atnibam.pms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 规格值表
 *
 * @TableName spec_value
 */
@TableName(value = "spec_value")
@Data
public class SpecValue implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 规格值id
     */
    @TableId(type = IdType.AUTO)
    private Integer specValueId;
    /**
     * 规格名id
     */
    private Integer specNameId;
    /**
     * 规格值
     */
    private String specValueName;
    /**
     * 规格值图
     */
    private String specValueImageUrl;
    /**
     * 显示顺序
     */
    private Integer displayOrder;
}