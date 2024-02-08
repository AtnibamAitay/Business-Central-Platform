package space.atnibam.pms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * SPU封面表
 *
 * @TableName spu_cover
 */
@TableName(value = "spu_cover")
@Data
public class SpuCover implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * spu封面id
     */
    @TableId(type = IdType.AUTO)
    private Integer coverId;
    /**
     * 商品id
     */
    private Integer spuId;
    /**
     * 封面url
     */
    private String coverUrl;
    /**
     * 显示顺序
     */
    private Integer displayOrder;
}