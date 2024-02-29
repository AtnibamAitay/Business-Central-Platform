package space.atnibam.api.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: LoginDTO
 * @Description: 登录响应实体
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-21 13:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户登录响应实体")
@Builder
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户登录令牌")
    private String accessToken;
}