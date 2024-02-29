package space.atnibam.api.auth.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SessionUserInfoDTO
 * @Description: Session中保存的用户信息
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-29 15:22
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Session中保存的用户信息DTO")
public class SessionUserInfoDTO {
    /**
     * 用户id
     */
    private Integer userId;
}