package space.atnibam.ums.mapper;

import lombok.Data;

/**
 * @ClassName: UserBasicInfoDTO
 * @Description: 用户基础信息数据传输对象，用于封装用户的基本信息如ID、用户名和头像URL
 * @Author: AtnibamAitay
 * @CreateTime: 2024-2-15 15:01
 **/
@Data
public class UserBasicInfoDTO {
    /**
     * 用户唯一标识符
     */
    private int userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像的URL地址
     */
    private String userAvatar;
}