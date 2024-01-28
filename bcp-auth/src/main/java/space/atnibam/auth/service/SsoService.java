package space.atnibam.auth.service;

import space.atnibam.auth.model.dto.AccountVerificationDTO;
import space.atnibam.common.core.domain.R;
import space.atnibam.common.core.domain.dto.LoginRequestDTO;
import space.atnibam.common.core.domain.vo.UserInfoVO;
import space.atnibam.common.core.exception.SystemServiceException;

import java.io.IOException;

/**
 * 用户单点登录服务接口
 */
public interface SsoService {
    /**
     * 发送验证码
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @return 返回验证码发送结果
     */
    R sendCode(AccountVerificationDTO accountVerificationDTO);

    /**
     * 通过邮箱/手机号、验证码和应用ID进行单点登录
     *
     * @param loginRequestDTO 登录请求数据传输对象
     * @throws IOException 输入输出异常
     */
    void ssoLoginByCodeHandler(LoginRequestDTO loginRequestDTO) throws IOException;

    /**
     * 根据凭据ID和应用代码查询用户信息
     *
     * @param credentialsId 凭据ID
     * @param appCode       应用代码
     * @return 用户信息视图对象
     * @throws SystemServiceException 用户信息不存在异常
     */
    UserInfoVO queryUserInfoByCredentialsId(String credentialsId, String appCode) throws SystemServiceException;
}
