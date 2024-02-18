package space.atnibam.ims.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: UserBaseInfoDTO
 * @Description: 基本的用户信息
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-18 19:00
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 头像
     */
    private String userAvatar;
    /**
     * 用户名
     */
    private String userName;
}