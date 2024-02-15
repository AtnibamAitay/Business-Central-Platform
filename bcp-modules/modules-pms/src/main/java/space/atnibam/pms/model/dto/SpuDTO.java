package space.atnibam.pms.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: SpuDTO
 * @Description: 商品信息
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-09 00:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpuDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品id
     */
    private Integer spuId;
    /**
     * 封面列表
     */
    private List<String> cover;
    /**
     * 商户
     */
    private MerchantDTO merchant;
    /**
     * 商品名
     */
    private String name;
    /**
     * 综合评分
     */
    // TODO
    private Double totalComprehensiveScore;
    /**
     * 累计销量
     */
    private Integer salesVolume;
    /**
     * 发货地
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shipmentOrigin;
    /**
     * 运费
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal shippingFee;
    /**
     * 规格
     */
    // TODO
    private List<specDTO> spec;
    /**
     * 介绍图列表
     */
    private List<String> detail;
    /**
     * 是否上架（0表示下架、1表示）
     */
    private Integer isListed;

    /**
     * 商户
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MerchantDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 商户ID
         */
        private Integer userId;
        /**
         * 用户名
         */
        private String userName;
        /**
         * 头像
         */
        private String userAvatar;
        /**
         * 商户简介
         */
        private String userIntroduction;
    }

    /**
     * 规格
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class specDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 规格名
         */
        private String specName;
        /**
         * 规格值
         */
        private List<specValueDTO> specValue;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class specValueDTO implements Serializable {
            private static final long serialVersionUID = 1L;
            /**
             * 规格值id
             */
            private Integer specValueId;
            /**
             * 规格值名
             */
            private String specValueName;
            /**
             * 规格值图url
             */
            private String specValueImg;
        }
    }
}
