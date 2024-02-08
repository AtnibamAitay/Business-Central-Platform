package space.atnibam.pms.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 *
 * @TableName spu
 */
@TableName(value = "spu")
@Data
public class Spu implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
    private Integer spuId;
    /**
     * 商户id
     */
    private Integer merchantId;
    /**
     * 商品名
     */
    private String name;
    /**
     * 累计销量
     */
    private Integer salesVolume;
    /**
     * 发货地
     */
    private String shipmentOrigin;
    /**
     * 运费
     */
    private BigDecimal shippingFee;
    /**
     * 商品创建时间
     */
    private Date creationTime;
    /**
     * 是否上架（0表示下架、1表示）
     */
    private Integer isListed;
    /**
     * 状态码（0代表已删除，1代表正常）
     */
    private Integer statusCode;
}