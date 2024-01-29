package space.atnibam.api.ums;

import space.atnibam.common.core.domain.AuthCredentials;
import space.atnibam.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户凭证远程服务接口
 */
@FeignClient(value = "modules-ums", contextId = "userCredentials", url = "http://local.atnibam.space:9030")
public interface RemoteUserCredentialsService {

    /**
     * 根据邮箱获取用户凭证
     *
     * @param email 邮箱
     * @return 用户凭证
     */
    @GetMapping("/api/users/email/{email}")
    R<AuthCredentials> getAuthCredentialsByEmail(@PathVariable(value = "email") String email);

    /**
     * 根据手机号获取用户凭证
     *
     * @param phone 手机号
     * @return 用户凭证
     */
    @GetMapping("/api/users/phone/{phone}")
    R<AuthCredentials> getAuthCredentialsByPhone(@PathVariable(value = "phone") String phone);

    /**
     * 通过手机号创建用户凭证
     *
     * @param certificate 手机号码证书
     * @return 用户凭证
     */
    @PostMapping("/api/users/phone")
    R createUserCredentialsByPhone(@RequestBody String certificate);

    /**
     * 通过邮箱创建用户
     *
     * @param certificate 邮箱
     * @return 创建结果
     */
    @PostMapping("/api/users/email")
    R createUserCredentialsByEmail(@RequestBody String certificate);
}
