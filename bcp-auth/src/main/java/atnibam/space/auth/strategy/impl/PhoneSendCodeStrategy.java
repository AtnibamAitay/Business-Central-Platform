package atnibam.space.auth.strategy.impl;

import atnibam.space.auth.model.dto.AccountVerificationDTO;
import atnibam.space.auth.service.AuthCredentialsService;
import atnibam.space.auth.strategy.sendCodeStrategy;
import atnibam.space.auth.utils.SmsUtil;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.ValidatorUtil;
import atnibam.space.common.redis.utils.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static atnibam.space.auth.constant.AuthConstants.CODE_TTL;
import static atnibam.space.auth.constant.AuthConstants.LOGIN_PHONE_CODE_KEY;
import static atnibam.space.common.core.enums.ResultCode.PHONE_NUM_NON_COMPLIANCE;

@Component
public class PhoneSendCodeStrategy implements sendCodeStrategy {
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
        isValidPhoneNumberFormat(phoneNumber);
        // 判断用户是否存在
        authCredentialsService.checkPhoneExist(phoneNumber);
        // TODO:实现线程池异步发送
        smsUtil.sendMsg(phoneNumber, code, accountVerificationDTO.getContent());
    }

    /**
     * 校验手机号是否符合规范
     *
     * @param phoneNumber 手机号
     */
    private void isValidPhoneNumberFormat(String phoneNumber) {
        if (!ValidatorUtil.isMobile(phoneNumber)) {
            throw new UserOperateException(PHONE_NUM_NON_COMPLIANCE);
        }
    }

    /**
     * 保存验证码到Redis中
     *
     * @param email 邮箱
     * @param code  验证码
     */
    @Override
    public void saveVerificationCodeToRedis(String email, String code) {
        cacheClient.setWithLogicalExpire(LOGIN_PHONE_CODE_KEY + email, code, CODE_TTL, TimeUnit.MINUTES);
    }
}
