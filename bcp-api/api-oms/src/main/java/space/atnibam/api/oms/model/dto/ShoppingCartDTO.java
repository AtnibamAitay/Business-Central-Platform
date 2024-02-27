package space.atnibam.api.oms.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "商品DTO", description = "用于展示商品详细信息的数据传输对象")
@Data
public class ShoppingCartDTO {

    @ApiModelProperty(value = "商品ID", required = true, example = "10001")
    private Integer spuId;

    @ApiModelProperty(value = "商品名称", required = true, example = "iPhone 13 Pro Max")
    private String name;

    @ApiModelProperty(value = "商品价格（单位：元）", required = true, example = "8999.00")
    private Double price;

    @ApiModelProperty(value = "商品数量", required = true, example = "5")
    private Integer quantity;

    @ApiModelProperty(value = "商品封面图URL", required = true, example = "https://example.com/product-cover.jpg")
    private String coverImage;

    @ApiModelProperty(value = "商品库存数量", required = true, example = "100")
    private Integer stockQuantity;
}