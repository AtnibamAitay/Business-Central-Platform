package space.atnibam.common.core.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BindingCertificateDTO {
    @NotNull(message = "账号ID不能为空")
    private Integer credentialsId;

    @NotBlank(message = "绑定账号不能为空")
    private String certificate;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @NotNull(message = "应用ID不能为空")
    @ApiModelProperty("应用ID")
    private String appId;
}
