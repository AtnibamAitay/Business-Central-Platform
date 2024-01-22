package atnibam.space.api.user;

import atnibam.space.common.core.domain.AuthCredentials;
import atnibam.space.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户凭证远程服务接口
 */
@FeignClient(value = "modules-user", contextId = "userCredentials")
public interface RemoteUserCredentialsService {

    /**
     * 根据邮箱获取用户凭证
     *
     * @param email 邮箱
     * @return 用户凭证
     */
    @RequestMapping(method = RequestMethod.GET, value = "/user/email/{email}")
    R<AuthCredentials> getAuthCredentialsByEmail(@PathVariable(value = "email") String email);

    /**
     * 根据手机号获取用户凭证
     *
     * @param phone 手机号
     * @return 用户凭证
     */
    @RequestMapping(method = RequestMethod.GET, value = "/user/phone/{phone}")
    R<AuthCredentials> getAuthCredentialsByPhone(@PathVariable(value = "phone") String phone);

    /**
     * 通过手机号创建用户凭证
     *
     * @param certificate 手机号码证书
     * @return 用户凭证
     */
    @RequestMapping(method = RequestMethod.POST, value = "/user/phone")
    R createUserCredentialsByPhone(@RequestBody String certificate);

    /**
     * 通过邮箱创建用户凭证
     *
     * @param certificate 邮箱证书
     * @return 用户凭证
     */
    @RequestMapping(method = RequestMethod.POST, value = "/user/email")
    R createUserCredentialsByEmail(@RequestBody String certificate);
}
