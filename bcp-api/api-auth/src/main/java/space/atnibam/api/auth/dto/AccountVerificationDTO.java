package space.atnibam.api.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel("接收发送验证码的对象以及类型")
public class AccountVerificationDTO {

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty("邮箱/手机号")
    private String accountNumber;

    @NotNull(message = "验证码类型不能为空")
    @ApiModelProperty("1-手机号登录验证码、2-邮箱登录验证码、3-绑定手机号验证码、4-绑定邮箱验证码")
    private Integer codeType;

    @ApiModelProperty("验证码邮件标题")
    private String title;

    @NotNull(message = "验证码短信/邮件内容不能为空")
    @ApiModelProperty("验证码短信/邮件内容")
    private String content;

    @NotNull(message = "应用ID不能为空")
    @ApiModelProperty("应用ID")
    private String appId;
}