package space.atnibam.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.atnibam.common.core.domain.AuthCredentials;

/**
 * 针对表【auth_credentials】的数据库操作Mapper
 */
@Mapper
public interface AuthCredentialsMapper extends BaseMapper<AuthCredentials> {
}




