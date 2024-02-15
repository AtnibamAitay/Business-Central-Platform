package space.atnibam.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import space.atnibam.common.core.domain.UserInfo;
import space.atnibam.ums.model.dto.UserInfoDTO;

import java.util.List;

/**
 * 针对表【user_info】的数据库操作Mapper
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    /**
     * 恢复默认信息
     *
     * @param userId 用户ID
     */
    void recoverDefaultInfo(Integer userId);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email   邮箱
     * @param appCode 应用代码
     * @return 用户信息
     */
    UserInfo queryUserInfoByEmail(String email, String appCode);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone   手机号
     * @param appCode 应用代码
     * @return 用户信息
     */
    UserInfo queryUserInfoByPhone(String phone, String appCode);

    /**
     * 更新用户头像
     *
     * @param userId    用户ID
     * @param avatarUrl 新的头像URL
     * @return 更新成功返回1，否则返回0
     */
    @Update("UPDATE user_info SET user_avatar = #{avatarUrl} WHERE credentials_id = #{userId}")
    int updateAvatarById(int userId, String avatarUrl);

    /**
     * 根据用户id列表查出用户名和用户头像（基础信息）
     *
     * @param userIds 用户ID列表
     * @return 用户基础信息列表
     */
    @Select("<script>SELECT user_id, user_name, user_avatar FROM user_info WHERE user_id IN " +
            "<foreach item='item' index='index' collection='userIds' open='(' separator=',' close=')'>#{item}</foreach></script>")
    List<UserBasicInfoDTO> selectBasicUserInfoByIds(List<Integer> userIds);

    /**
     * 根据用户id查出用户名、用户头像以及用户简介（详细信息）
     *
     * @param userId 用户ID
     * @return 用户详细信息
     */
    @Select("SELECT user_id, user_name, user_avatar, user_introduction FROM user_info WHERE user_id = #{userId}")
    UserInfoDTO selectDetailedUserInfoById(int userId);
}
