package space.atnibam.ums.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UserInfoDTO
 * @Description: 用户信息
 * @Author: AtnibamAitay
 * @CreateTime: 2024-02-09 14:03
 **/
@Data
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID-主键
     */
    private Integer userId;
    /**
     * 用户名-默认值(应该是随机生成)
     */
    private String userName;
    /**
     * 头像-默认（固定默认头像）
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userIntroduction;
}
