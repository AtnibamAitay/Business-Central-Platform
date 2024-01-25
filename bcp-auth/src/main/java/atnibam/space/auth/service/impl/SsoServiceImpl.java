package atnibam.space.auth.service.impl;

import atnibam.space.api.user.RemoteUserInfoService;
import atnibam.space.auth.factory.CertificateStrategyFactory;
import atnibam.space.auth.factory.SendCodeStrategyFactory;
import atnibam.space.auth.model.dto.AccountVerificationDTO;
import atnibam.space.auth.service.SsoService;
import atnibam.space.auth.strategy.CertificateStrategy;
import atnibam.space.auth.strategy.SendCodeStrategy;
import atnibam.space.common.core.constant.UserConstants;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.R;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import atnibam.space.common.core.domain.vo.UserInfoVO;
import atnibam.space.common.core.enums.CertificateMethodEnum;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.SystemServiceException;
import atnibam.space.common.core.exception.UserOperateException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

import static atnibam.space.common.core.constant.Constants.RANDOM_LENGTH;

/**
 * 单点登录服务类
 */
@Service
public class SsoServiceImpl implements SsoService {
    @Autowired
    private RemoteUserInfoService remoteUserInfoService;
    @Autowired
    private CertificateStrategyFactory certificateStrategyFactory;
    @Autowired
    private SendCodeStrategyFactory sendCodeStrategyFactory;

    /**
     * 发送验证码
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @return 返回验证码发送结果
     */
    @Override
    public R sendCode(AccountVerificationDTO accountVerificationDTO) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(RANDOM_LENGTH);
        // TODO:临时强制设定验证码为123456，生产环境注意删除此段代码
        code = "123456";
        // 根据登录方式决定策略
        SendCodeStrategy strategy = sendCodeStrategyFactory.getStrategy(CertificateMethodEnum.fromCode(accountVerificationDTO.getCodeType()));
        // 发送验证码
        strategy.sendCodeHandler(accountVerificationDTO, code);
        // 把验证码加入到缓存中
        strategy.saveVerificationCodeToRedis(accountVerificationDTO.getAccountNumber(), code, accountVerificationDTO.getAppId());
        return R.ok();
    }

    /**
     * 通过邮箱/手机号、验证码和应用ID进行单点登录
     *
     * @param loginRequestDTO 登录请求数据传输对象
     * @throws IOException 输入输出异常
     */
    @Override
    public void SsoLoginByCodeHandler(LoginRequestDTO loginRequestDTO) throws IOException {
        CertificateStrategy certificateStrategy = certificateStrategyFactory.getLoginStrategy(CertificateMethodEnum.fromCode(loginRequestDTO.getLoginMethod()));

        String code = certificateStrategy.getCodeFromRedis(loginRequestDTO.getCertificate(), loginRequestDTO.getAppCode());
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
        // 根据证书获取认证凭证
        AuthCredentials credentials = certificateStrategy.getAuthCredentialsByCertificate(loginRequestDTO.getCertificate());
        // 如果认证凭证为空
        if (Objects.isNull(credentials)) {
            // 根据证书创建认证凭证
            certificateStrategy.createCredentialsByCertificate(loginRequestDTO.getCertificate());
            // 创建用户信息对象
            UserInfo userInfo = new UserInfo();
            // 设置用户信息的凭证ID为刚刚创建的认证凭证的凭证ID
            userInfo.setCredentialsId(certificateStrategy.getAuthCredentialsByCertificate(loginRequestDTO.getCertificate()).getCredentialsId());
            // 设置用户信息的应用代码
            userInfo.setAppCode(loginRequestDTO.getAppCode());
            // 注册用户信息
            remoteUserInfoService.registration(userInfo);
            // 返回空值
            return null;
        }
        // 根据证书获取用户信息
        UserInfo userInfo = certificateStrategy.getUserInfoByCertificate(loginRequestDTO);
        // 如果用户信息为空
        if (Objects.isNull(userInfo)) {
            // 创建用户信息对象
            userInfo = new UserInfo();
            // 设置用户信息的凭证ID为刚刚获取的认证凭证的凭证ID
            userInfo.setCredentialsId(credentials.getCredentialsId());
            // 设置用户信息的应用代码
            userInfo.setAppCode(loginRequestDTO.getAppCode());
            // 注册用户信息
            remoteUserInfoService.registration(userInfo);
            // 返回空值
            return null;
        }
        // 返回用户信息
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
    @Override
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
