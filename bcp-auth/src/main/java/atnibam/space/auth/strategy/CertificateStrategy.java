package atnibam.space.auth.strategy;

import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;

/**
 * 证书策略接口
 */
public interface CertificateStrategy {
    /**
     * 从Redis获取账号的验证码
     *
     * @param accountNumber 账号
     * @return 验证码
     */
    String getCodeFromRedis(String accountNumber, String appId);

    /**
     * 根据账号信息获取用户信息
     *
     * @param loginRequestDTO 登录请求数据
     * @return 用户信息
     */
    UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO);

    /**
     * 根据账号获取认证凭证
     *
     * @param accountNumber 账号
     * @return 认证凭证
     */
    AuthCredentials getAuthCredentialsByCertificate(String accountNumber);

    /**
     * 创建通过账号生成的认证凭证
     *
     * @param accountNumber 账号
     */
    void createCredentialsByCertificate(String accountNumber);
}
