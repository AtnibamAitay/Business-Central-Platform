package atnibam.space.user.service;

import atnibam.space.common.core.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
 * 针对表【user_info】的数据库操作Service
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfo queryUserInfo(String userId);

    /**
     * 注销用户登录
     *
     * @param userId 用户ID
     */
    void logout(String userId);

    /**
     * 用户注册
     *
     * @param userInfo 用户信息
     * @throws IOException IO异常
     */
    void registration(UserInfo userInfo) throws IOException;

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     */
    void updateUserInfo(UserInfo userInfo);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email   邮箱
     * @param appCode 应用代码
     * @return 用户信息
     */
    UserInfo queryUserInfoByEmail(String email, String appCode);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone   手机号
     * @param appCode 应用代码
     * @return 用户信息
     */
    UserInfo queryUserInfoByPhone(String phone, String appCode);

    /**
     * 根据认证ID查询用户信息
     *
     * @param credentialsId 认证ID
     * @param appCode       应用代码
     * @return 用户信息
     */
    UserInfo queryUserInfoByCredentialsId(String credentialsId, String appCode);
}

