package atnibam.space.auth.strategy;

import atnibam.space.auth.model.dto.AccountVerificationDTO;

/**
 * 证书策略接口
 */
public interface sendCodeStrategy {
    /**
     * 发送验证码处理方法
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @param code                   验证码
     */
    void sendCodeHandler(AccountVerificationDTO accountVerificationDTO, String code);

    /**
     * 将验证码保存到Redis
     *
     * @param account 账号
     * @param code    验证码
     */
    void saveVerificationCodeToRedis(String account, String code);
}
