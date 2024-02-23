package space.atnibam.api.ums.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateUserNameDTO {
    @NotNull(message = "账号ID不能为空")
    private Integer credentialsId;

    @NotBlank(message = "用户名不能为空")
    private String userName;
}