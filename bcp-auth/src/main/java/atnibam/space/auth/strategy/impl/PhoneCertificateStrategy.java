package atnibam.space.auth.strategy.impl;

import atnibam.space.api.ums.RemoteUserCredentialsService;
import atnibam.space.api.ums.RemoteUserInfoService;
import atnibam.space.auth.strategy.CertificateStrategy;
import atnibam.space.auth.utils.SmsUtil;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.StringUtils;
import atnibam.space.common.redis.service.RedisCache;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static atnibam.space.auth.constant.AuthConstants.LOGIN_PHONE_CODE_KEY;
import static atnibam.space.common.core.enums.ResultCode.USER_VERIFY_ERROR;

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
    @Autowired
    private SmsUtil smsUtil;

    /**
     * 根据手机号获取认证凭证
     *
     * @param phoneNumber 手机号
     * @return 认证凭证
     */
    @Override
    public AuthCredentials getAuthCredentialsByCertificate(String phoneNumber) {
        // 判断手机号格式是否正确
        smsUtil.isValidPhoneNumberFormat(phoneNumber);
        return remoteUserCredentialsService.getAuthCredentialsByPhone(phoneNumber).getData();
    }

    /**
     * 根据手机号和登录请求获取用户信息
     *
     * @param loginRequestDTO 登录请求
     * @return 用户信息
     */
    @Override
    public UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO) {
        // 判断手机号格式是否正确
        smsUtil.isValidPhoneNumberFormat(loginRequestDTO.getAccountNumber());
        return remoteUserInfoService.getUserInfoByPhone(loginRequestDTO.getAccountNumber(), loginRequestDTO.getAppId()).getData();
    }

    /**
     * 根据手机号创建用户凭证
     *
     * @param phoneNumber 手机号
     */
    @Override
    public void createCredentialsByCertificate(String phoneNumber) {
        // 判断手机号格式是否正确
        smsUtil.isValidPhoneNumberFormat(phoneNumber);
        remoteUserCredentialsService.createUserCredentialsByPhone(phoneNumber);
    }

    /**
     * 从Redis中获取手机验证码
     *
     * @param phoneNumber 手机号
     * @return 手机验证码
     * @throws UserOperateException 用户操作异常
     */
    @Override
    public String getCodeFromRedis(String phoneNumber, String appId) {
        JSONObject cacheResult = redisCache.getCacheObject(LOGIN_PHONE_CODE_KEY + phoneNumber);
        // 检查结果是否存在且数据部分不为空
        if (cacheResult != null && !cacheResult.isEmpty()) {
            // 获取结果中的数据部分并转换为JSONObject对象
            JSONObject dataObject = cacheResult.getJSONObject("data");
            // 检查数据部分是否为空
            if (dataObject != null && dataObject.getString("code") != null) {
                // 获取数据部分中的appId字段值
                String appIdCache = dataObject.getString("appId");
                // 检查appId字段值是否与传入的appId相等
                if (!appId.equals(appIdCache)) {
                    throw new UserOperateException(USER_VERIFY_ERROR);
                }
                // 获取数据部分中的code字段值
                String code = dataObject.getString("code");
                // 检查code字段值是否包含文本
                if (StringUtils.hasText(code)) {
                    return code;
                }
            }
        }

        //todo log
        throw new UserOperateException(USER_VERIFY_ERROR);
    }

    /**
     * 删除缓存中的验证码
     *
     * @param accountNumber 账号
     */
    @Override
    public void deleteCodeFromRedis(String accountNumber) {
        redisCache.deleteObject(LOGIN_PHONE_CODE_KEY + accountNumber);
    }
}
