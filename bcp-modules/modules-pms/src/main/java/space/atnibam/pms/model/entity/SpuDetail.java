package space.atnibam.pms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * SPU详细表
 *
 * @TableName spu_detail
 */
@TableName(value = "spu_detail")
@Data
public class SpuDetail implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * spu详细介绍id
     */
    @TableId(type = IdType.AUTO)
    private Integer detailId;
    /**
     * 商品id
     */
    private Integer spuId;
    /**
     * spu详细图文url
     */
    private String detailUrl;
    /**
     * 显示顺序
     */
    private Integer displayOrder;
}