package space.atnibam.api.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("登陆注册请求类")
@Data
@NoArgsConstructor
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 凭证 手机号登录凭证为手机号码，邮箱登录凭证为邮箱
     */
    @ApiModelProperty("邮箱或手机号")
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不能为空")
    private String accountNumber;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    @NotNull(message = "验证码不能为空")
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    /**
     * 登录方式
     * 1-手机号+验证码
     * 2-邮箱+验证码
     */
    @ApiModelProperty("登录方式 1-手机号+验证码 2-邮箱+验证码")
    @NotNull(message = "登录方式不能为空")
    private Integer loginMethod;

    /**
     * 应用码
     */
    @ApiModelProperty("应用ID")
    @NotNull(message = "应用ID不能为空")
    @NotBlank(message = "应用ID不能为空")
    private String appId;
}