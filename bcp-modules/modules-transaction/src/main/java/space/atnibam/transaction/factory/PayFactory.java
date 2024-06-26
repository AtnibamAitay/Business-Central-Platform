package space.atnibam.transaction.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import space.atnibam.transaction.bridge.*;

import javax.annotation.Resource;

/**
 * @ClassName: PayFactory
 * @Description: 支付工厂类，根据支付类型生成相应的支付实例
 * @Author: atnibamaitay
 * @CreateTime: 2023-09-13 17:39:28
 **/
@Component
public class PayFactory {

    /**
     * 支付宝APP支付类型常量
     */
    public static final int ALI_PAY_NATIVE = 1;
    /**
     * 微信APP支付类型常量
     */
    public static final int WE_CHAT_APP = 2;
    /**
     * 微信NATIVE支付类型常量
     */
    public static final int WX_PAY_NATIVE = 3;
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 根据支付类型创建对应的支付实例
     *
     * @param type 支付类型，可以是支付宝APP支付、微信APP支付或者微信H5支付
     * @return 返回对应的支付实例，如果支付类型不符合要求，则返回null
     */
    public AbstractPay createPay(int type) {
        switch (type) {
            case ALI_PAY_NATIVE:
                AliPayNative aliPayNative = applicationContext.getBean(AliPayNative.class);
                return new AliPay(aliPayNative);
            case WX_PAY_NATIVE:
                WxPayNative wxPayNative = applicationContext.getBean(WxPayNative.class);
                return new WxPay(wxPayNative);
            default:
                return null;
        }
    }
}