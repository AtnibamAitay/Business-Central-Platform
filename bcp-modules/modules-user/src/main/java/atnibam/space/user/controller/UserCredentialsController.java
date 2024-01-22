package atnibam.space.user.controller;

import atnibam.space.api.user.RemoteUserCredentialsService;
import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.R;
import atnibam.space.common.core.domain.dto.BindingCertificateDTO;
import atnibam.space.user.service.AuthCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: UserCredentialsController
 * @Description: 用户凭证控制层
 * @Author: AtnibamAitay
 * @CreateTime: 2024-01-18 15:29
 **/
@RestController
@RequestMapping("/api/users")
public class UserCredentialsController implements RemoteUserCredentialsService {
    @Autowired
    private AuthCredentialsService authCredentialsService;

    /**
     * 根据邮箱获取用户凭证
     *
     * @param email 邮箱
     * @return 用户凭证
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/email/{email}")
    public R<AuthCredentials> getAuthCredentialsByEmail(@PathVariable(value = "email") String email) {
        AuthCredentials authCredentials = authCredentialsService.queryAuthCredentialsByEmail(email);
        return R.ok(authCredentials);
    }

    /**
     * 根据手机号获取用户凭证
     *
     * @param phone 手机号
     * @return 用户凭证
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/phone/{phone}")
    public R<AuthCredentials> getAuthCredentialsByPhone(@PathVariable(value = "phone") String phone) {
        AuthCredentials authCredentials = authCredentialsService.queryAuthCredentialsByPhone(phone);
        return R.ok(authCredentials);
    }

    /**
     * 通过手机号创建用户凭证
     *
     * @param certificate 手机号码证书
     * @return 用户凭证
     */
    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/phone")
    public R createUserCredentialsByPhone(@RequestBody String certificate) {
        authCredentialsService.createUserCredentialsByPhone(certificate);
        return R.ok();
    }

    /**
     * 通过邮箱创建用户凭证
     *
     * @param certificate 邮箱证书
     * @return 用户凭证
     */
    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/email")
    public R createUserCredentialsByEmail(@RequestBody String certificate) {
        authCredentialsService.createUserCredentialsByEmail(certificate);
        return R.ok();
    }

    /**
     * 通过手机号绑定用户凭证
     *
     * @param bindingCertificateDTO 绑定证书
     * @return 结果
     */
    @RequestMapping(method = RequestMethod.POST, value = "/phone/binding")
    public R bindingPhoneById(@Validated @RequestBody BindingCertificateDTO bindingCertificateDTO) {
        authCredentialsService.bindingPhoneById(bindingCertificateDTO);
        return R.ok();
    }

    /**
     * 通过邮箱绑定用户凭证
     *
     * @param bindingCertificateDTO 绑定证书
     * @return 结果
     */
    @RequestMapping(method = RequestMethod.POST, value = "/email/binding")
    public R bindingEmailById(@Validated @RequestBody BindingCertificateDTO bindingCertificateDTO) {
        authCredentialsService.bindingEmailById(bindingCertificateDTO);
        return R.ok();
    }
}
