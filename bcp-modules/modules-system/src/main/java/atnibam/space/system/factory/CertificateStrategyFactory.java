package atnibam.space.system.factory;

import atnibam.space.common.core.enums.CertificateMethodEnum;
import atnibam.space.system.strategy.CertificateStrategy;
import atnibam.space.system.strategy.impl.EmailCertificateStrategy;
import atnibam.space.system.strategy.impl.PhoneCertificateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 证书策略工厂
 */
@Component
public class CertificateStrategyFactory {
    @Autowired
    private PhoneCertificateStrategy phoneCertificateStrategy;
    @Autowired
    private EmailCertificateStrategy emailCertificateStrategy;

    /**
     * 获取证书策略
     *
     * @param certificateMethodEnum 证书方法
     * @return 证书策略
     */
    public CertificateStrategy getStrategy(CertificateMethodEnum certificateMethodEnum) {
        return createCodeHandlerStrategy(certificateMethodEnum);
    }

    /**
     * 根据证书方法创建证书策略
     *
     * @param certificateMethodEnum 证书方法
     * @return 证书策略
     */
    private CertificateStrategy createCodeHandlerStrategy(CertificateMethodEnum certificateMethodEnum) {

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