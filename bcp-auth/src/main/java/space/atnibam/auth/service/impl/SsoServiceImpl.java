package space.atnibam.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.auth.factory.CertificateStrategyFactory;
import space.atnibam.auth.factory.SendCodeStrategyFactory;
import space.atnibam.auth.model.dto.AccountVerificationDTO;
import space.atnibam.auth.service.SsoService;
import space.atnibam.auth.strategy.CertificateStrategy;
import space.atnibam.auth.strategy.SendCodeStrategy;
import space.atnibam.common.core.domain.AuthCredentials;
import space.atnibam.common.core.domain.R;
import space.atnibam.common.core.domain.UserInfo;
import space.atnibam.common.core.domain.dto.LoginRequestDTO;
import space.atnibam.common.core.domain.vo.UserInfoVO;
import space.atnibam.common.core.enums.CertificateMethodEnum;
import space.atnibam.common.core.enums.ResultCode;
import space.atnibam.common.core.exception.SystemServiceException;
import space.atnibam.common.core.exception.UserOperateException;

import java.io.IOException;
import java.util.Objects;

import static space.atnibam.common.core.constant.Constants.RANDOM_LENGTH;
import static space.atnibam.common.core.constant.UserConstants.CANCEL_LOGOUT;
import static space.atnibam.common.core.constant.UserConstants.LOGOUT;
import static space.atnibam.common.core.enums.ResultCode.USER_VERIFY_ERROR;

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
        // 根据验证码类型决定策略
        SendCodeStrategy strategy = sendCodeStrategyFactory.getStrategy(CertificateMethodEnum.fromCode(accountVerificationDTO.getCodeType()));
        // 发送验证码
        strategy.sendCodeHandler(accountVerificationDTO, code);
        // 把验证码加入到缓存中
        strategy.saveVerificationCodeToRedis(accountVerificationDTO.getAccountNumber(), code, accountVerificationDTO.getCodeType(), accountVerificationDTO.getAppId());
        return R.ok();
    }

    /**
     * 通过邮箱/手机号、验证码和应用ID进行单点登录
     *
     * @param loginRequestDTO 登录请求数据传输对象
     * @throws IOException 输入输出异常
     */
    @Override
    public void ssoLoginByCodeHandler(LoginRequestDTO loginRequestDTO) throws IOException {
        // 根据登录请求DTO中的登录方法创建策略
        CertificateStrategy certificateStrategy = certificateStrategyFactory.getLoginStrategy(CertificateMethodEnum.fromCode(loginRequestDTO.getLoginMethod()));

        // 从Redis中获取验证码，并与登录请求中的验证码进行比较
        String code = certificateStrategy.getCodeFromRedis(loginRequestDTO.getAccountNumber(), loginRequestDTO.getAppId());
        if (!loginRequestDTO.getVerifyCode().equals(code)) {
            // 验证码校验失败
            throw new UserOperateException(USER_VERIFY_ERROR);
        }
        // 删除缓存中的验证码
        certificateStrategy.deleteCodeFromRedis(loginRequestDTO.getAccountNumber());
        // 1.如未注册进行注册 2.如注销状态取消注销
        checkUserLogoutStatus(registrationHandler(certificateStrategy, loginRequestDTO));

        // 使用证书策略根据登录请求获取用户信息
        UserInfo userInfo = certificateStrategy.getUserInfoByCertificate(loginRequestDTO);
        if (Objects.isNull(userInfo)) {
            // 用户信息为空，抛出系统服务异常
            throw new SystemServiceException(ResultCode.USER_NOT_EXIST_BY_CODE);
        }
        // 登录用户
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
        AuthCredentials credentials = certificateStrategy.getAuthCredentialsByCertificate(loginRequestDTO.getAccountNumber());
        // 如果认证凭证为空
        if (Objects.isNull(credentials)) {
            // 根据证书创建认证凭证
            certificateStrategy.createCredentialsByCertificate(loginRequestDTO.getAccountNumber());
            // 创建用户信息对象
            UserInfo userInfo = new UserInfo();
            // 设置用户信息的凭证ID为刚刚创建的认证凭证的凭证ID
            // TODO:这一步可以改为，在根据账号创建用户的时候，就返回这个CredentialsId
            userInfo.setCredentialsId(certificateStrategy.getAuthCredentialsByCertificate(loginRequestDTO.getAccountNumber()).getCredentialsId());
            // 设置用户信息的应用代码
            // TODO:对应中台来说，一个账号应该需要能够登录所有前台应用，因此可以考虑去掉为用户信息绑定APPID
            userInfo.setAppCode(loginRequestDTO.getAppId());
            // 注册用户信息
            remoteUserInfoService.registration(userInfo);
            // 返回空值
            // TODO:为什么返回的是空值呢
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
            userInfo.setAppCode(loginRequestDTO.getAppId());
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
        if (!Objects.isNull(userInfo) && LOGOUT.equals(userInfo.getLogoutStatus().toString())) {
            userInfo.setLogoutStatus(Integer.valueOf(CANCEL_LOGOUT));
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
