package atnibam.space.system.strategy.impl;

import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.ValidatorUtil;
import atnibam.space.common.redis.utils.CacheClient;
import atnibam.space.system.model.dto.AccountVerificationDTO;
import atnibam.space.system.service.AuthCredentialsService;
import atnibam.space.system.strategy.CertificateStrategy;
import atnibam.space.system.utils.EmailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static atnibam.space.common.core.enums.ResultCode.EMAIL_NUM_NON_COMPLIANCE;
import static atnibam.space.system.constant.AuthConstants.CODE_TTL;
import static atnibam.space.system.constant.AuthConstants.LOGIN_EMAIL_CODE_KEY;

/**
 * 邮箱证书策略类
 */
@Component
public class EmailCertificateStrategy implements CertificateStrategy {
    @Autowired
    private CacheClient redisCache;
    @Resource
    private EmailSenderUtil emailSenderUtil;
    @Resource
    private AuthCredentialsService authCredentialsService;

    /**
     * 发送验证码处理方法
     *
     * @param accountVerificationDTO 包含邮箱、邮箱内容等信息的数据传输对象
     * @param code                   验证码
     */
    @Override
    public void sendCodeHandler(AccountVerificationDTO accountVerificationDTO, String code) {
        String email = accountVerificationDTO.getAccountNumber();
        // 检查邮箱是否合法
        isValidEmailFormat(email);
        // 判断用户是否存在
        authCredentialsService.checkEmailExist(email);
        // 发送邮件
        // TODO:实现线程池异步发送
        emailSenderUtil.sendSimpleMail(email, accountVerificationDTO.getTitle(), accountVerificationDTO.getContent());
    }

    /**
     * 保存验证码到Redis中
     *
     * @param email 邮箱
     * @param code  验证码
     */
    @Override
    public void saveVerificationCodeToRedis(String email, String code) {
        redisCache.setWithLogicalExpire(LOGIN_EMAIL_CODE_KEY + email, code, CODE_TTL, TimeUnit.MINUTES);
    }

    /**
     * 判断邮箱是否为合法邮箱格式
     *
     * @param email 邮箱
     * @throws UserOperateException 若不是合法邮箱格式，则抛出用户操作异常
     */
    private void isValidEmailFormat(String email) {
        if (!ValidatorUtil.isEmail(email)) {
            throw new UserOperateException(EMAIL_NUM_NON_COMPLIANCE);
        }
    }
}
