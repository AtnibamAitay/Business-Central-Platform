package atnibam.space.auth.strategy.impl;

import atnibam.space.api.user.RemoteUserCredentialsService;
import atnibam.space.api.user.RemoteUserInfoService;
import atnibam.space.auth.strategy.CertificateStrategy;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.StringUtils;
import atnibam.space.common.core.utils.ValidatorUtil;
import atnibam.space.common.redis.constant.CacheConstants;
import atnibam.space.common.redis.service.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邮件证书策略组件
 */
@Component
public class EmailCertificateStrategy implements CertificateStrategy {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RemoteUserCredentialsService remoteUserCredentialsService;
    @Autowired
    private RemoteUserInfoService remoteUserInfoService;

    /**
     * 从Redis中获取邮件验证码
     *
     * @param certificate 证书
     * @return 邮件验证码
     * @throws UserOperateException 用户操作异常
     */
    @Override
    public String getCodeFromRedis(String certificate) {
        checkCertificate(certificate);
        String emailCode = redisCache.getCacheObject(CacheConstants.LOGIN_CODE_KEY + CacheConstants.EMAIL_KEY + certificate);
        if (!StringUtils.hasText(emailCode)) {
            //todo log
            throw new UserOperateException(ResultCode.USER_VERIFY_ERROR);
        }
        return emailCode;
    }

    /**
     * 根据证书获取认证凭证
     *
     * @param certificate 证书
     * @return 认证凭证
     */
    @Override
    public AuthCredentials getAuthCredentialsByCertificate(String certificate) {
        checkCertificate(certificate);
        return remoteUserCredentialsService.getAuthCredentialsByEmail(certificate).getData();
    }

    /**
     * 根据证书获取用户信息
     *
     * @param loginRequestDTO 登录请求数据
     * @return 用户信息
     */
    @Override
    public UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO) {
        checkCertificate(loginRequestDTO.getCertificate());
        return remoteUserInfoService.getUserInfoByEmail(loginRequestDTO.getCertificate(), loginRequestDTO.getAppCode()).getData();
    }

    /**
     * 创建凭证
     *
     * @param certificate 证书
     */
    @Override
    public void createCredentialsByCertificate(String certificate) {
        checkCertificate(certificate);
        remoteUserCredentialsService.createUserCredentialsByEmail(certificate);
    }

    /**
     * 检查证书
     *
     * @param certificate 证书
     * @throws UserOperateException 用户操作异常
     */
    private void checkCertificate(String certificate) {
        if (!ValidatorUtil.isEmail(certificate)) {
            throw new UserOperateException(ResultCode.EMAIL_NUM_NON_COMPLIANCE);
        }
    }
}
