package space.atnibam.transaction.service;

import space.atnibam.transaction.model.entity.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author Atnibam Aitay
* @description 针对表【payment_info】的数据库操作Service
* @createDate 2023-09-07 14:15:09
*/
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 记录微信支付信息
     * 使用 Gson 将传入的 plainText 转换为 HashMap 来获取订单号、业务编号、支付类型、交易状态以及用户实际支付金额等信息，
     * 然后创建一个 PaymentInfo 对象，将这些信息设置到此对象中，并存入数据库。
     *
     * @param plainText Json格式的字符串，包含需要记录的支付信息
     */
    void createPaymentInfo(String plainText);

    /**
     * 记录支付日志：支付宝
     *
     * @param params 支付相关参数，包括订单号、业务编号、交易状态、交易金额等信息
     */
    void createPaymentInfoForAliPay(Map<String, String> params);
}
