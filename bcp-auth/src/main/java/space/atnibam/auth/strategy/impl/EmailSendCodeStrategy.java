package space.atnibam.auth.strategy.impl;

import space.atnibam.auth.model.dto.AccountVerificationDTO;
import space.atnibam.auth.strategy.SendCodeStrategy;
import space.atnibam.auth.utils.EmailUtil;
import space.atnibam.common.redis.utils.CacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static space.atnibam.auth.constant.AuthConstants.CODE_TTL;
import static space.atnibam.auth.constant.AuthConstants.LOGIN_EMAIL_CODE_KEY;

/**
 * 邮箱证书策略类
 */
@Component
public class EmailSendCodeStrategy implements SendCodeStrategy {
    /**
     * 确保StringTemplateResolver仅初始化一次
     */
    private final StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
    @Autowired
    private CacheClient redisCache;
    @Resource
    private EmailUtil emailUtil;
    @Resource
    private TemplateEngine templateEngine;

    @Autowired
    public EmailSendCodeStrategy(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @PostConstruct
    public void init() {
        stringTemplateResolver.setTemplateMode("HTML");
        templateEngine.addTemplateResolver(stringTemplateResolver);
    }

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
        emailUtil.isValidEmailFormat(email);
        // 发送邮件
        // TODO:实现线程池异步发送
        emailUtil.sendSimpleMail(email, accountVerificationDTO.getTitle(), buildEmailContent(accountVerificationDTO, code));
    }

    /**
     * 保存验证码到Redis中
     *
     * @param email 邮箱
     * @param code  验证码
     */
    @Override
    public void saveVerificationCodeToRedis(String email, String code, String appId) {
        Map<String, String> verificationData = new HashMap<>();
        verificationData.put("appId", appId);
        verificationData.put("code", code);
        redisCache.setWithLogicalExpire(LOGIN_EMAIL_CODE_KEY + email, verificationData, CODE_TTL, TimeUnit.MINUTES);
    }

    /**
     * 构建邮件内容
     *
     * @param accountVerificationDTO 包含邮箱、邮箱内容等信息的数据传输对象
     * @param code                   验证码
     * @return 邮件内容
     */
    private String buildEmailContent(AccountVerificationDTO accountVerificationDTO, String code) {
        Context context = new Context();
        context.setVariable("email", accountVerificationDTO.getAccountNumber());
        context.setVariable("verifyCode", code);
        context.setVariable("ttl", CODE_TTL);

        // 使用字符串形式的HTML模板内容
        String emailTemplateContent = accountVerificationDTO.getContent();

        // 处理字符串模板并返回结果
        return templateEngine.process(emailTemplateContent, context);
    }
}
