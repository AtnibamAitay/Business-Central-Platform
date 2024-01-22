package atnibam.space.system.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 发送邮箱验证码工具类
 */
@Component
public class SendEmailUtil {

    public static final String SUFFIX = "，为了您的账号安全，请勿外泄，该验证码5分钟内有效";
    //根据配置文件中自己的QQ邮箱
    private static final String FROM = "1151214239@qq.com";

    public String loginPrefix = "验证码为:";
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 根据给定的前缀、内容和主题生成邮件内容
     *
     * @param prefix  验证码前缀
     * @param content 邮件内容
     * @param subject 邮件主题
     * @return 邮件内容
     */
    public static String getContent(String prefix, String content, String subject) {
        // 生成邮件内容
        return subject + prefix + content + SUFFIX;
    }

    /**
     * 根据给定的内容和主题生成邮件内容
     *
     * @param content 邮件内容
     * @param subject 邮件主题
     * @return 邮件内容
     */
    public String getContent(String content, String subject) {
        // 调用静态方法getContent生成邮件内容
        return getContent(loginPrefix, content, subject);
    }

    /**
     * 发送简单邮件
     *
     * @param to      接收者邮箱
     * @param subject 邮件主题
     * @param code    邮件内容中的验证码
     */
    public void sendSimpleMail(String to, String subject, String code) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件发送者
        message.setFrom(FROM);
        //设置邮件接收人
        message.setTo(to);
        //设置邮件主题
        message.setSubject(subject);
        //设置邮件内容
        message.setText(getContent(code, subject));
        //通过JavaMailSender类把邮件发送出去
        javaMailSender.send(message);
    }
}