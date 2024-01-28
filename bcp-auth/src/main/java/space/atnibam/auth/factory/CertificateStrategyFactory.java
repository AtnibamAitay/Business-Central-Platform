package space.atnibam.auth.factory;

import space.atnibam.auth.strategy.CertificateStrategy;
import space.atnibam.auth.strategy.impl.EmailCertificateStrategy;
import space.atnibam.auth.strategy.impl.PhoneCertificateStrategy;
import space.atnibam.common.core.enums.CertificateMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateStrategyFactory {
    @Autowired
    private PhoneCertificateStrategy phoneLoginStrategy;
    @Autowired
    private EmailCertificateStrategy emailLoginStrategy;

    /**
     * 获取登录策略
     *
     * @param certificateMethodEnum 认证方法枚举
     * @return 对应的登录策略
     */
    public CertificateStrategy getLoginStrategy(CertificateMethodEnum certificateMethodEnum) {
        return createLoginStrategy(certificateMethodEnum);
    }

    /**
     * 创建登录策略
     *
     * @param certificateMethodEnum 认证方法枚举
     * @return 对应的登录策略
     */
    private CertificateStrategy createLoginStrategy(CertificateMethodEnum certificateMethodEnum) {
        switch (certificateMethodEnum) {
            case EMAIL_CODE:
                return emailLoginStrategy;
            case PHONE_CODE:
                return phoneLoginStrategy;
            default:
                throw new IllegalArgumentException("Unsupported login method: " + certificateMethodEnum);
        }
    }
}
