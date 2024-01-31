package space.atnibam.ums.model.dto;

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

    // FIXME: 不太对，因为账号是各个应用通用的，验证应用ID的意义是什么呢？
    @NotNull(message = "应用ID不能为空")
    @ApiModelProperty("应用ID")
    private String appId;
}
