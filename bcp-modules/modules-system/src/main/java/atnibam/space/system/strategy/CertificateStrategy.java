package atnibam.space.system.strategy;

import atnibam.space.common.core.constant.UserConstants;
import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.SystemServiceException;
import atnibam.space.common.redis.constant.CacheConstants;

/**
 * 证书策略接口
 */
public interface CertificateStrategy {
    /**
     * 根据类型获取Redis key前缀
     *
     * @param integer 类型
     * @return Redis key前缀
     * @throws SystemServiceException 如果类型无效，则抛出异常
     */
    static String getPrefixByType(Integer integer) {
        switch (integer) {
            case UserConstants.PREFIX_TYPE_1:
                return CacheConstants.LOGIN_CODE_KEY;
            case UserConstants.PREFIX_TYPE_2:
                return CacheConstants.BINDING_CODE_KEY;
            default:
                throw new SystemServiceException(ResultCode.PARAM_IS_INVALID);
        }
    }

    /**
     * 发送验证码处理方法
     *
     * @param account 账号
     * @param code    码
     * @param subject 主题
     */
    void sendCodeHandler(String account, String code, String subject);

    /**
     * 将验证码保存到Redis
     *
     * @param verifyCodeDTO 验证码信息
     * @param code          码
     */
    void saveVerificationCodeToRedis(VerifyCodeDTO verifyCodeDTO, String code);
}
