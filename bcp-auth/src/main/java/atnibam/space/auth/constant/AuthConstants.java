package atnibam.space.auth.constant;

/**
 * @ClassName: AuthConstants
 * @Description: 认证相关常量
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-23 09:05
 **/
public class AuthConstants {
    /**
     * 验证码存活时间，单位为分钟
     */
    public static final Long CODE_TTL = 5L;

    /**
     * 登录时的邮箱验证码的键
     */
    public static final String LOGIN_EMAIL_CODE_KEY = "login:code:email:";

    /**
     * 登录时的手机号验证码的键
     */
    public static final String LOGIN_PHONE_CODE_KEY = "login:code:phone:";

}