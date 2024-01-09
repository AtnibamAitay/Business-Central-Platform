package atnibam.space.auth.factory;

import atnibam.space.auth.strategy.impl.EmailCertificateStrategy;
import atnibam.space.auth.strategy.impl.PhoneCertificateStrategy;
import atnibam.space.common.core.enums.CertificateMethodEnum;
import atnibam.space.auth.strategy.CertificateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: gaojianjie
 * @Description
 * @date 2023/9/11 08:29
 */
@Component
public class CertificateStrategyFactory {
    @Autowired
    private PhoneCertificateStrategy phoneLoginStrategy;
    @Autowired
    private EmailCertificateStrategy emailLoginStrategy;

    public CertificateStrategy getLoginStrategy(CertificateMethodEnum certificateMethodEnum) {
        return createLoginStrategy(certificateMethodEnum);
    }
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
