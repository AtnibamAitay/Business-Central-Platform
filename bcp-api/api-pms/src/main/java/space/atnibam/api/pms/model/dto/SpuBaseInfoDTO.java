package space.atnibam.api.pms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: SpuBaseInfoDTO
 * @Description: 商品基本信息
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-23 10:19
 **/
@Data
public class SpuBaseInfoDTO {
    /**
     * 商品id
     */
    private Integer spuId;
    /**
     * 商品名
     */
    private String name;
    /**
     * 商户基础信息
     */
    private MerchantBaseInfoDTO merchant;

    /**
     * 商户基础信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MerchantBaseInfoDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 商户ID
         */
        private Integer userId;
        /**
         * 用户名
         */
        private String userName;
    }
}
