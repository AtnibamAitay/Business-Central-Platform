package atnibam.space.system.enums;

/**
 * @ClassName: LoginMethodEnum
 * @Description: 登录方法枚举类
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-19 14:21
 **/
public enum LoginMethodEnum {

    /**
     * 通过手机号和验证码登录
     */
    PHONE_CODE(1),

    /**
     * 通过邮箱和验证码登录
     */
    EMAIL_CODE(2);

    /**
     * 登录方式的代码
     */
    private final int code;

    /**
     * 构造函数，用于初始化登录方式的代码
     *
     * @param code 登录方式的代码
     */
    LoginMethodEnum(int code) {
        this.code = code;
    }

    /**
     * 根据代码返回相应的登录方式
     *
     * @param code 登录方式的代码
     * @return 对应的登录方式
     * @throws IllegalArgumentException 如果输入的代码没有对应的登录方式则抛出此异常
     */
    public static LoginMethodEnum fromCode(int code) {
        // 遍历所有的登录方式
        for (LoginMethodEnum method : LoginMethodEnum.values()) {
            // 如果找到了对应的登录方式则返回
            if (method.getCode() == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("无效的登录方法代码: " + code);
    }

    /**
     * 获取登录方式的代码
     *
     * @return 登录方式的代码
     */
    public int getCode() {
        return code;
    }
}