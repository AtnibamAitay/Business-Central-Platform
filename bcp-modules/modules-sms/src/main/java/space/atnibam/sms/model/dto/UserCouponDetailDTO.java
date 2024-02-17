package space.atnibam.sms.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: UserCouponDetailDTO
 * @Description: 优惠券详情DTO
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-18 00:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCouponDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 优惠券ID
     */
    private Integer couponId;
    /**
     * 优惠券名
     */
    private String couponName;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date startDate;
    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;
    /**
     * 满减券门槛
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MinSpendThresholdsDTO minSpendThresholds;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MinSpendThresholdsDTO {
        /**
         * 最低订单金额
         */
        private BigDecimal minOrderAmount;
        /**
         * 优惠金额
         */
        private BigDecimal discountAmount;
    }
}