package space.atnibam.auth.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.atnibam.auth.strategy.SendCodeStrategy;
import space.atnibam.auth.strategy.impl.EmailSendCodeStrategy;
import space.atnibam.auth.strategy.impl.PhoneSendCodeStrategy;
import space.atnibam.common.core.enums.CertificateMethodEnum;

/**
 * 证书策略工厂
 */
@Component
public class SendCodeStrategyFactory {
    @Autowired
    private PhoneSendCodeStrategy phoneCertificateStrategy;
    @Autowired
    private EmailSendCodeStrategy emailCertificateStrategy;

    /**
     * 获取证书策略
     *
     * @param certificateMethodEnum 证书方法
     * @return 证书策略
     */
    public SendCodeStrategy getStrategy(CertificateMethodEnum certificateMethodEnum) {
        return createCodeHandlerStrategy(certificateMethodEnum);
    }

    /**
     * 根据证书方法创建证书策略
     *
     * @param certificateMethodEnum 证书方法
     * @return 证书策略
     */
    private SendCodeStrategy createCodeHandlerStrategy(CertificateMethodEnum certificateMethodEnum) {
        switch (certificateMethodEnum) {
            case PHONE_CODE:
            case BINDING_PHONE_CODE:
                return phoneCertificateStrategy;
            case EMAIL_CODE:
            case BINDING_EMAIL_CODE:
                return emailCertificateStrategy;
            default:
                throw new IllegalArgumentException("不支持的认证类型: " + certificateMethodEnum);
        }
    }
}