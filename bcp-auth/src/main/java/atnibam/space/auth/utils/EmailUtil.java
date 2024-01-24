package atnibam.space.auth.utils;

import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static atnibam.space.common.core.enums.ResultCode.EMAIL_NUM_NON_COMPLIANCE;

/**
 * @ClassName: EmailUtil
 * @Description: 发送邮件工具类
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-23 19:58
 **/
@Component
public class EmailUtil {
    @Value("${spring.mail.username}")
    public String EMAIL_FROM;
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 判断邮箱是否为合法邮箱格式
     *
     * @param email 邮箱
     * @throws UserOperateException 若不是合法邮箱格式，则抛出用户操作异常
     */
    public void isValidEmailFormat(String email) {
        if (!ValidatorUtil.isEmail(email)) {
            throw new UserOperateException(EMAIL_NUM_NON_COMPLIANCE);
        }
    }

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param title   邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String title, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //邮件发送
        try {
            //创建SimpleMailMessage对象
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            //发件人
            message.setFrom(EMAIL_FROM);
            //收件人
            message.setTo(to);
            //邮件标题
            message.setSubject(title);
            //邮件内容
            message.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //通过JavaMailSender类把邮件发送出去
        javaMailSender.send(mimeMessage);
    }
}