package atnibam.space.auth.strategy;

import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;

/**
 * @Author: gaojianjie
 * @date 2023/9/11 08:23
 */
public interface CertificateStrategy {
    String getCodeFromRedis(String certificate);
    UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO);
    AuthCredentials getAuthCredentialsByCertificate(String certificate);
    void createCredentialsByCertificate(String certificate);
}
