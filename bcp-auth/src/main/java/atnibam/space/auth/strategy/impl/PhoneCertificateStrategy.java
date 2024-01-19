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
 * 手机验证码认证策略类
 */
@Component
public class PhoneCertificateStrategy implements CertificateStrategy {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RemoteUserCredentialsService remoteUserCredentialsService;
    @Autowired
    private RemoteUserInfoService remoteUserInfoService;

    /**
     * 根据证书获取认证凭证
     *
     * @param certificate 证书
     * @return 认证凭证
     */
    @Override
    public AuthCredentials getAuthCredentialsByCertificate(String certificate) {
        checkCertificate(certificate);
        return remoteUserCredentialsService.getAuthCredentialsByPhone(certificate).getData();
    }

    /**
     * 根据证书和登录请求获取用户信息
     *
     * @param loginRequestDTO 登录请求
     * @return 用户信息
     */
    @Override
    public UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO) {
        checkCertificate(loginRequestDTO.getCertificate());
        return remoteUserInfoService.getUserInfoByPhone(loginRequestDTO.getCertificate(), loginRequestDTO.getAppCode()).getData();
    }

    /**
     * 根据证书创建用户凭证
     *
     * @param certificate 证书
     */
    @Override
    public void createCredentialsByCertificate(String certificate) {
        checkCertificate(certificate);
        remoteUserCredentialsService.createUserCredentialsByPhone(certificate);
    }

    /**
     * 从Redis中获取手机验证码
     *
     * @param certificate 证书
     * @return 手机验证码
     * @throws UserOperateException 用户操作异常
     */
    @Override
    public String getCodeFromRedis(String certificate) {
        String phoneCode = redisCache.getCacheObject(CacheConstants.LOGIN_CODE_KEY + CacheConstants.PHONE_KEY + certificate);
        if (!StringUtils.hasText(phoneCode)) {
            throw new UserOperateException(ResultCode.USER_VERIFY_ERROR);
        }
        return phoneCode;
    }

    /**
     * 检查证书是否合法
     *
     * @param certificate 证书
     * @throws UserOperateException 用户操作异常
     */
    private void checkCertificate(String certificate) {
        if (!ValidatorUtil.isMobile(certificate)) {
            throw new UserOperateException(ResultCode.PHONE_NUM_NON_COMPLIANCE);
        }
    }
}
