package atnibam.space.auth.strategy;

import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;

/**
 * 证书策略接口
 */
public interface CertificateStrategy {
    /**
     * 从Redis获取证书代码
     *
     * @param certificate 证书
     * @return 证书代码
     */
    String getCodeFromRedis(String certificate);

    /**
     * 根据证书获取用户信息
     *
     * @param loginRequestDTO 登录请求数据
     * @return 用户信息
     */
    UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO);

    /**
     * 根据证书获取认证凭证
     *
     * @param certificate 证书
     * @return 认证凭证
     */
    AuthCredentials getAuthCredentialsByCertificate(String certificate);

    /**
     * 创建通过证书生成的认证凭证
     *
     * @param certificate 证书
     */
    void createCredentialsByCertificate(String certificate);
}
