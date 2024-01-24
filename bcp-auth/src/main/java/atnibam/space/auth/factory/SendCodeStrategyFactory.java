package atnibam.space.auth.factory;

import atnibam.space.auth.strategy.impl.EmailSendCodeStrategy;
import atnibam.space.auth.strategy.impl.PhoneSendCodeStrategy;
import atnibam.space.auth.strategy.sendCodeStrategy;
import atnibam.space.common.core.enums.CertificateMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public sendCodeStrategy getStrategy(CertificateMethodEnum certificateMethodEnum) {
        return createCodeHandlerStrategy(certificateMethodEnum);
    }

    /**
     * 根据证书方法创建证书策略
     *
     * @param certificateMethodEnum 证书方法
     * @return 证书策略
     */
    private sendCodeStrategy createCodeHandlerStrategy(CertificateMethodEnum certificateMethodEnum) {
        switch (certificateMethodEnum) {
            case EMAIL_CODE:
                return emailCertificateStrategy;
            case PHONE_CODE:
                return phoneCertificateStrategy;
            default:
                throw new IllegalArgumentException("不支持的认证类型: " + certificateMethodEnum);
        }
    }
}