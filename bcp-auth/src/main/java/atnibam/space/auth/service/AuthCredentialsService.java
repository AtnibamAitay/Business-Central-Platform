package atnibam.space.auth.service;

import atnibam.space.auth.model.entity.AuthCredentials;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Atnibam Aitay
 * @description 针对表【auth_credentials】的数据库操作Service
 * @createDate 2024-01-23 17:39:07
 */
public interface AuthCredentialsService extends IService<AuthCredentials> {
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     */
    void checkEmailExist(String email);

    /**
     * 检查手机号是否存在
     *
     * @param phoneNumber 手机号
     */
    void checkPhoneExist(String phoneNumber);
}