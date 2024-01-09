package atnibam.space.system.strategy.impl;

import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.ValidatorUtil;
import atnibam.space.common.redis.constant.CacheConstants;
import atnibam.space.common.redis.service.RedisCache;
import atnibam.space.system.utils.SendEmailUtil;
import atnibam.space.system.strategy.CertificateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: gaojianjie
 * @date 2023/9/11 08:26
 */
@Component
public class EmailCertificateStrategy implements CertificateStrategy {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SendEmailUtil sendEmailUtil;

    @Override
    public void sendCodeHandler(String account, String code,String subject) {
        checkCertificate(account);
        sendEmailUtil.sendSimpleMail(account,subject,code);
    }

    @Override
    public void saveVerificationCodeToRedis(VerifyCodeDTO verifyCodeDTO, String code) {
        String prefix = CertificateStrategy.getPrefixByType(verifyCodeDTO.getFunctionType());
        checkCertificate(verifyCodeDTO.getAccount());
        redisCache.setCacheObject(prefix+ CacheConstants.EMAIL_KEY+verifyCodeDTO.getAccount(),code,CacheConstants.MESSAGE_CODE_TIME_OUT, TimeUnit.MINUTES);
    }
    private void checkCertificate(String certificate){
        if(!ValidatorUtil.isEmail(certificate)){
            throw new UserOperateException(ResultCode.EMAIL_NUM_NON_COMPLIANCE);
        }
    }

}
