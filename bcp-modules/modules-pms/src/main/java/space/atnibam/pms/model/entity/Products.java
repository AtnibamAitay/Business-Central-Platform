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
 * @ClassName: Products
 * @Description: 商品表
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-29 22:44
 **/
@TableName(value = "products")
@Data
public class Products implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
    private Integer productId;
    /**
     * 商品封面
     */
    private String coverImage;
    /**
     * 商户id-外键
     */
    private Integer merchantId;
    /**
     * 商品名
     */
    private String productName;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 下单人数
     */
    private Integer orderCount;
    /**
     * 库存
     */
    private Integer inventory;
    /**
     * 商品创建时间
     */
    private Date creationTime;
    /**
     * 状态码（0代表已删除，1代表正常）
     */
    private Integer statusCode;
}