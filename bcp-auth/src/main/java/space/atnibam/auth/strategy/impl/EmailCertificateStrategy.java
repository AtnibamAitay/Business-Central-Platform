package space.atnibam.auth.strategy.impl;

import space.atnibam.api.ums.RemoteUserCredentialsService;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.auth.strategy.CertificateStrategy;
import space.atnibam.auth.utils.EmailUtil;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.StringUtils;
import space.atnibam.common.redis.service.RedisCache;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static space.atnibam.auth.constant.AuthConstants.LOGIN_EMAIL_CODE_KEY;
import static atnibam.space.common.core.enums.ResultCode.USER_VERIFY_ERROR;

/**
 * 邮件证书策略组件
 */
@Slf4j
@Component
public class EmailCertificateStrategy implements CertificateStrategy {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RemoteUserCredentialsService remoteUserCredentialsService;
    @Autowired
    private RemoteUserInfoService remoteUserInfoService;
    @Resource
    private EmailUtil emailUtil;

    /**
     * 从Redis中获取邮箱的验证码
     *
     * @param email 邮箱
     * @return 验证码
     * @throws UserOperateException 用户操作异常
     */
    @Override
    public String getCodeFromRedis(String email, String appId) {
        // 检查邮箱是否合法
        emailUtil.isValidEmailFormat(email);
        JSONObject cacheResult = redisCache.getCacheObject(LOGIN_EMAIL_CODE_KEY + email);
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
        redisCache.deleteObject(LOGIN_EMAIL_CODE_KEY + accountNumber);
    }

    /**
     * 根据邮箱获取认证凭证
     *
     * @param email 邮箱
     * @return 认证凭证
     */
    @Override
    public AuthCredentials getAuthCredentialsByCertificate(String email) {
        // 检查邮箱是否合法
        emailUtil.isValidEmailFormat(email);
        return remoteUserCredentialsService.getAuthCredentialsByEmail(email).getData();
    }

    /**
     * 根据证书获取用户信息
     *
     * @param loginRequestDTO 登录请求数据
     * @return 用户信息
     */
    @Override
    public UserInfo getUserInfoByCertificate(LoginRequestDTO loginRequestDTO) {
        // 检查邮箱是否合法
        emailUtil.isValidEmailFormat(loginRequestDTO.getAccountNumber());
        return remoteUserInfoService.getUserInfoByEmail(loginRequestDTO.getAccountNumber(), loginRequestDTO.getAppId()).getData();
    }

    /**
     * 通过邮箱创建用户
     *
     * @param email 邮箱地址
     */
    @Override
    public void createCredentialsByCertificate(String email) {
        // 检查邮箱是否合法
        emailUtil.isValidEmailFormat(email);
        remoteUserCredentialsService.createUserCredentialsByEmail(email);
    }
}
