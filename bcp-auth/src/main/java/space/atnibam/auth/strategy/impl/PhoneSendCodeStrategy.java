package space.atnibam.auth.strategy.impl;

import space.atnibam.auth.model.dto.AccountVerificationDTO;
import space.atnibam.auth.service.AuthCredentialsService;
import space.atnibam.auth.strategy.SendCodeStrategy;
import space.atnibam.auth.utils.SmsUtil;
import space.atnibam.common.redis.utils.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static space.atnibam.auth.constant.AuthConstants.CODE_TTL;
import static space.atnibam.auth.constant.AuthConstants.LOGIN_PHONE_CODE_KEY;

@Component
public class PhoneSendCodeStrategy implements SendCodeStrategy {
    @Autowired
    private SmsUtil smsUtil;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private AuthCredentialsService authCredentialsService;

    /**
     * 发送验证码处理方法
     *
     * @param accountVerificationDTO 包含手机号、短信内容等信息的数据传输对象
     * @param code                   验证码
     */
    @Override
    public void sendCodeHandler(AccountVerificationDTO accountVerificationDTO, String code) {
        String phoneNumber = accountVerificationDTO.getAccountNumber();
        // 判断手机号格式是否正确
        smsUtil.isValidPhoneNumberFormat(phoneNumber);
        // TODO:实现线程池异步发送
        smsUtil.sendMsg(phoneNumber, code, accountVerificationDTO.getContent());
    }

    /**
     * 保存验证码到Redis中
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     */
    @Override
    public void saveVerificationCodeToRedis(String phoneNumber, String code, String appId) {
        Map<String, String> verificationData = new HashMap<>();
        verificationData.put("appId", appId);
        verificationData.put("code", code);
        cacheClient.setWithLogicalExpire(LOGIN_PHONE_CODE_KEY + phoneNumber, verificationData, CODE_TTL, TimeUnit.MINUTES);
    }
}
