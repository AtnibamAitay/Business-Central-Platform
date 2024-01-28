package space.atnibam.ums.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAvatarDTO {
    @NotNull(message = "账号ID不能为空")
    private Integer credentialsId;

    @NotBlank(message = "头像不能为空")
    private MultipartFile avatar;
}