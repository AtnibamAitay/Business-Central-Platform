package atnibam.space.user.mapper;

import atnibam.space.common.core.domain.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}




