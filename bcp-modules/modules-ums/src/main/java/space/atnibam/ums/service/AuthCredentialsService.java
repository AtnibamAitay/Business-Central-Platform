package space.atnibam.ums.service;

import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.dto.BindingCertificateDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 针对表【auth_credentials】的数据库操作Service
 */
public interface AuthCredentialsService extends IService<AuthCredentials> {
    /**
     * 根据手机号判断用户是否存在
     *
     * @param phoneNumber 手机号
     * @return AuthCredentials 用户认证凭证
     */
    AuthCredentials checkPhoneNumbExit(String phoneNumber);

    /**
     * 根据邮箱获取用户账号数据
     *
     * @param email 邮箱
     * @return AuthCredentials 用户账号数据
     */
    AuthCredentials queryAuthCredentialsByEmail(String email);

    /**
     * 根据手机号查询用户认证凭证
     *
     * @param phone 手机号
     * @return AuthCredentials 用户认证凭证
     */
    AuthCredentials queryAuthCredentialsByPhone(String phone);

    /**
     * 根据手机号创建用户认证凭证
     *
     * @param certificate 手机认证证书
     */
    void createUserCredentialsByPhone(String certificate);

    /**
     * 通过邮箱创建用户
     *
     * @param certificate 邮箱
     */
    void createUserCredentialsByEmail(String certificate);

    /**
     * 根据用户ID绑定手机号
     *
     * @param bindingCertificateDTO 绑定证书信息
     */
    void bindingPhoneById(BindingCertificateDTO bindingCertificateDTO);

    /**
     * 根据用户ID绑定邮箱
     *
     * @param bindingCertificateDTO 绑定证书信息
     */
    void bindingEmailById(BindingCertificateDTO bindingCertificateDTO);
}
