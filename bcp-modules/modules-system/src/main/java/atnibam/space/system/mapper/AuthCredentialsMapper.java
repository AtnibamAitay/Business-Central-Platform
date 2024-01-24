package atnibam.space.system.mapper;

import atnibam.space.system.model.entity.AuthCredentials;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Atnibam Aitay
 * @description 针对表【auth_credentials】的数据库操作Mapper
 * @createDate 2024-01-23 17:39:07
 * @Entity atnibam.space.system.model.entity.AuthCredentials
 */
public interface AuthCredentialsMapper extends BaseMapper<AuthCredentials> {
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱地址
     * @return 如果邮箱存在返回1，否则返回0
     */
    @Select("SELECT EXISTS(SELECT 1 FROM auth_credentials WHERE email = #{email})")
    boolean checkEmailExist(String email);

    /**
     * 检查手机号是否存在
     *
     * @param phoneNumber 手机号
     * @return 如果手机号存在返回1，否则返回0
     */
    @Select("SELECT EXISTS(SELECT 1 FROM auth_credentials WHERE phone_number = #{phoneNumber})")
    boolean checkPhoneExist(String phoneNumber);
}