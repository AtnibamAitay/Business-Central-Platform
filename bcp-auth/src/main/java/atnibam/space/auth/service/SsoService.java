package atnibam.space.auth.service;

import atnibam.space.api.user.RemoteUserInfoService;
import atnibam.space.auth.factory.CertificateStrategyFactory;
import atnibam.space.auth.strategy.CertificateStrategy;
import atnibam.space.common.core.constant.UserConstants;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.domain.vo.UserInfoVO;
import atnibam.space.common.core.enums.CertificateMethodEnum;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.SystemServiceException;
import atnibam.space.common.core.exception.UserOperateException;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

/**
 * 单点登录服务类
 */
@Service
public class SsoService {
    @Autowired
    private RemoteUserInfoService remoteUserInfoService;
    @Autowired
    private CertificateStrategyFactory certificateStrategyFactory;

    /**
     * 通过代码处理单点登录登录请求
     *
     * @param loginRequestDTO 登录请求数据传输对象
     * @throws IOException 输入输出异常
     */
    public void SsoLoginByCodeHandler(LoginRequestDTO loginRequestDTO) throws IOException {
        CertificateStrategy certificateStrategy = certificateStrategyFactory.getLoginStrategy(CertificateMethodEnum.fromCode(loginRequestDTO.getLoginMethod()));

        String code = certificateStrategy.getCodeFromRedis(loginRequestDTO.getCertificate());
        if (!loginRequestDTO.getVerifyCode().equals(code)) {
            // 验证码校验失败
            throw new UserOperateException(ResultCode.USER_VERIFY_ERROR);
        }
        // redisCache.deleteObject(loginRequestDTO.getCertificate());
        // 1.如未注册进行注册 2.如注销状态取消注销
        checkUserLogoutStatus(registrationHandler(certificateStrategy, loginRequestDTO));
        UserInfo userInfo = certificateStrategy.getUserInfoByCertificate(loginRequestDTO);
        if (Objects.isNull(userInfo)) {
            // log.error
            throw new SystemServiceException(ResultCode.USER_NOT_EXIST_BY_CODE);
        }
        StpUtil.login(userInfo.getCredentialsId());
    }

    /**
     * 检查账户是否注册，没有则进行账户创建以及对应应用信息的默认初始化
     * 如账户存在而对应应用信息不存在则进行应用信息的默认初始化
     *
     * @param certificateStrategy 凭证策略
     * @param loginRequestDTO     登录请求数据传输对象
     * @return 用户信息
     * @throws IOException 输入输出异常
     */
    private UserInfo registrationHandler(CertificateStrategy certificateStrategy, LoginRequestDTO loginRequestDTO) throws IOException {
        AuthCredentials credentials = certificateStrategy.getAuthCredentialsByCertificate(loginRequestDTO.getCertificate());
        if (Objects.isNull(credentials)) {
            certificateStrategy.createCredentialsByCertificate(loginRequestDTO.getCertificate());
            UserInfo userInfo = new UserInfo();
            userInfo.setCredentialsId(certificateStrategy.getAuthCredentialsByCertificate(loginRequestDTO.getCertificate()).getCredentialsId());
            userInfo.setAppCode(loginRequestDTO.getAppCode());
            remoteUserInfoService.registration(userInfo);
            return null;
        }
        UserInfo userInfo = certificateStrategy.getUserInfoByCertificate(loginRequestDTO);
        if (Objects.isNull(userInfo)) {
            userInfo = new UserInfo();
            userInfo.setCredentialsId(credentials.getCredentialsId());
            userInfo.setAppCode(loginRequestDTO.getAppCode());
            remoteUserInfoService.registration(userInfo);
            return null;
        }
        return userInfo;
    }

    /**
     * 检查用户是否已注销
     *
     * @param userInfo 用户信息
     */
    private void checkUserLogoutStatus(UserInfo userInfo) {
        // 取消注销操作
        if (!Objects.isNull(userInfo) && UserConstants.LOGOUT.equals(userInfo.getLogoutStatus().toString())) {
            userInfo.setLogoutStatus(Integer.valueOf(UserConstants.CANCEL_LOGOUT));
            remoteUserInfoService.updateUserInfo(userInfo);
        }
    }

    /**
     * 根据凭据ID和应用代码查询用户信息
     *
     * @param credentialsId 凭据ID
     * @param appCode       应用代码
     * @return 用户信息视图对象
     */
    public UserInfoVO queryUserInfoByCredentialsId(String credentialsId, String appCode) {
        UserInfo userInfo = remoteUserInfoService.getUserInfoByCredentialsId(appCode, credentialsId).getData();
        UserInfoVO userInfoVO = new UserInfoVO();
        if (Objects.isNull(userInfo)) {
            throw new SystemServiceException(ResultCode.USERINFO_NON_EXIST);
        }
        BeanUtils.copyProperties(userInfo, userInfoVO);
        return userInfoVO;
    }
}
