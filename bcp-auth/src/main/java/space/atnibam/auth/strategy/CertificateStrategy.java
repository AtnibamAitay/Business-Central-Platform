package space.atnibam.auth.strategy;

import space.atnibam.auth.model.dto.LoginDTO;
import space.atnibam.common.core.domain.AuthCredentials;
import space.atnibam.common.core.domain.UserInfo;

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
     * 删除缓存中的验证码
     *
     * @param accountNumber 账号
     */
    void deleteCodeFromRedis(String accountNumber);

    /**
     * 根据账号信息获取用户信息
     *
     * @param loginDTO 登录请求数据
     * @return 用户信息
     */
    UserInfo getUserInfoByCertificate(LoginDTO loginDTO);

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
