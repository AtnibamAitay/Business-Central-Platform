package space.atnibam.api.auth;

import cn.dev33.satoken.util.SaResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.atnibam.api.auth.dto.AccountVerificationDTO;
import space.atnibam.api.auth.dto.LoginRequestDTO;
import space.atnibam.common.core.domain.R;

import java.io.IOException;

/**
 * @InterfaceName: RemoteAuthService
 * @Description: 远程认证服务
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-06 10:53
 **/
@FeignClient(value = "bcp-auth", contextId = "auth")
public interface RemoteAuthService {
    /**
     * 发送验证码
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @return Result 发送结果，成功发送则返回成功信息，否则返回失败原因
     */
    @ApiOperation(value = "发送验证码")
    @PostMapping("/api/auth/verification-codes")
    R sendCodeByAccount(@RequestBody @Validated AccountVerificationDTO accountVerificationDTO);

    /**
     * 单点登陆接口
     *
     * @param loginRequestDTO 登录请求DTO
     * @return 认证结果
     */
    @ApiOperation("单点登陆")
    @PostMapping("/api/auth/login")
    SaResult ssoLogin(@Validated @RequestBody LoginRequestDTO loginRequestDTO) throws IOException;
}