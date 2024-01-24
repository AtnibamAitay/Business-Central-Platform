package atnibam.space.common.core.enums;

/**
 * 用户认证方法枚举
 */
public enum CertificateMethodEnum {
    /**
     * 通过手机号和验证码登录
     */
    PHONE_CODE(1),
    /**
     * 通过邮箱和验证码登录
     */
    EMAIL_CODE(2);

    private final int code;

    /**
     * 构造方法
     *
     * @param code 认证方法代码
     */
    CertificateMethodEnum(int code) {
        this.code = code;
    }

    /**
     * 根据认证方法代码获取对应的认证方法
     *
     * @param code 认证方法代码
     * @return 对应的认证方法
     * @throws IllegalArgumentException 如果无效的认证方法代码
     */
    public static CertificateMethodEnum fromCode(int code) {
        for (CertificateMethodEnum method : CertificateMethodEnum.values()) {
            if (method.getCode() == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("无效的登录方式: " + code);
    }

    /**
     * 获取认证方法代码
     *
     * @return 认证方法代码
     */
    public int getCode() {
        return code;
    }
}