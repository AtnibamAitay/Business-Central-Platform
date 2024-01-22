package atnibam.space.system.strategy.impl;

import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.ValidatorUtil;
import atnibam.space.common.redis.constant.CacheConstants;
import atnibam.space.common.redis.service.RedisCache;
import atnibam.space.system.strategy.CertificateStrategy;
import atnibam.space.system.utils.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 邮箱证书策略类
 */
@Component
public class EmailCertificateStrategy implements CertificateStrategy {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SendEmailUtil sendEmailUtil;

    /**
     * 发送验证码处理方法
     *
     * @param account 邮箱账号
     * @param code    验证码
     * @param subject 主题
     */
    @Override
    public void sendCodeHandler(String account, String code, String subject) {
        // 检查证书
        checkCertificate(account);
        // 发送邮件
        sendEmailUtil.sendSimpleMail(account, subject, code);
    }

    /**
     * 将验证码保存到Redis中
     *
     * @param verifyCodeDTO 验证码信息
     * @param code          验证码
     */
    @Override
    public void saveVerificationCodeToRedis(VerifyCodeDTO verifyCodeDTO, String code) {
        // 获取前缀
        String prefix = CertificateStrategy.getPrefixByType(verifyCodeDTO.getFunctionType());
        // 检查证书
        checkCertificate(verifyCodeDTO.getAccount());
        // 保存验证码到Redis缓存中
        redisCache.setCacheObject(prefix + CacheConstants.EMAIL_KEY + verifyCodeDTO.getAccount(), code, CacheConstants.MESSAGE_CODE_TIME_OUT, TimeUnit.MINUTES);
    }

    /**
     * 检查证书方法，判断是否为合法邮箱格式
     *
     * @param certificate 证书（邮箱）
     * @throws UserOperateException 若不是合法邮箱格式，则抛出用户操作异常
     */
    private void checkCertificate(String certificate) {
        if (!ValidatorUtil.isEmail(certificate)) {
            throw new UserOperateException(ResultCode.EMAIL_NUM_NON_COMPLIANCE);
        }
    }
}
