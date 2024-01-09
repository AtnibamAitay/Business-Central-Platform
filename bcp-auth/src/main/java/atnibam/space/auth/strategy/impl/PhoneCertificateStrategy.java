package atnibam.space.auth.strategy.impl;

import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.utils.ValidatorUtil;
import atnibam.space.api.user.RemoteUserCredentialsService;
import atnibam.space.api.user.RemoteUserInfoService;
import atnibam.space.auth.strategy.CertificateStrategy;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.StringUtils;
import atnibam.space.common.redis.constant.CacheConstants;
import atnibam.space.common.redis.service.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: gaojianjie
 * @date 2023/9/11 08:26
 */
@Component
public class PhoneCertificateStrategy implements CertificateStrategy {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RemoteUserCredentialsService remoteUserCredentialsService;
    @Autowired
    private RemoteUserInfoService remoteUserInfoService;

    @Override
    public AuthCredentials getAuthCredentialsByCertificate(String certificate) {
        checkCertificate(certificate);
        return remoteUserCredentialsService.getAuthCredentialsByPhone(certificate).getData();
    }

    @Override
    public UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO) {
        checkCertificate(loginRequestDTO.getCertificate());
        return remoteUserInfoService.getUserInfoByPhone(loginRequestDTO.getCertificate(), loginRequestDTO.getAppCode()).getData();
    }

    @Override
    public void createCredentialsByCertificate(String certificate) {
        checkCertificate(certificate);
        remoteUserCredentialsService.createUserCredentialsByPhone(certificate);
    }

    @Override
    public String getCodeFromRedis(String certificate) {
        String phoneCode = redisCache.getCacheObject(CacheConstants.LOGIN_CODE_KEY+CacheConstants.PHONE_KEY+certificate);
        if(!StringUtils.hasText(phoneCode)){
            throw new UserOperateException(ResultCode.USER_VERIFY_ERROR);
        }
        return phoneCode;
    }

    private void checkCertificate(String certificate){
        if(!ValidatorUtil.isMobile(certificate)){
            throw new UserOperateException(ResultCode.PHONE_NUM_NON_COMPLIANCE);
        }
    }
}
