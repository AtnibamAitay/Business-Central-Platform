package cn.nfsn.auth.strategy.impl;

import cn.nfsn.api.user.RemoteUserCredentialsService;
import cn.nfsn.api.user.RemoteUserInfoService;
import cn.nfsn.auth.strategy.CertificateStrategy;
import cn.nfsn.common.core.domain.AuthCredentials;
import cn.nfsn.common.core.domain.UserInfo;
import cn.nfsn.common.core.domain.dto.LoginRequestDTO;
import cn.nfsn.common.core.enums.ResultCode;
import cn.nfsn.common.core.exception.UserOperateException;
import cn.nfsn.common.core.utils.StringUtils;
import cn.nfsn.common.redis.constant.CacheConstants;
import cn.nfsn.common.redis.service.RedisCache;
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
        return remoteUserCredentialsService.getAuthCredentialsByPhone(certificate).getData();
    }

    @Override
    public UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO) {
        return remoteUserInfoService.getUserInfoByPhone(loginRequestDTO.getCertificate(), loginRequestDTO.getAppCode()).getData();
    }

    @Override
    public void createCredentialsByCertificate(String certificate) {
        remoteUserCredentialsService.createUserCredentialsByPhone(certificate);
    }

    @Override
    public String getCodeFromRedis(String certificate) {
        String phoneCode = redisCache.getCacheObject(CacheConstants.LOGIN_PHONE_CODE_KEY+certificate);
        if(StringUtils.hasText(phoneCode)){
            //todo log
            throw new UserOperateException(ResultCode.USER_VERIFY_ERROR);
        }
        return phoneCode;
    }
}