package atnibam.space.auth.service;

import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.domain.vo.UserInfoVO;
import atnibam.space.common.core.exception.SystemServiceException;

import java.io.IOException;

/**
 * 用户单点登录服务接口
 */
public interface SsoService {

    /**
     * 通过邮箱/手机号、验证码和应用ID进行单点登录
     *
     * @param loginRequestDTO 登录请求数据传输对象
     * @throws IOException 输入输出异常
     */
    void SsoLoginByCodeHandler(LoginRequestDTO loginRequestDTO) throws IOException;

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
