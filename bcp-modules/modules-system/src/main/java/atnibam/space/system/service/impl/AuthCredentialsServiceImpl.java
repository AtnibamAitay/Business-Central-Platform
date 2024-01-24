package atnibam.space.system.service.impl;

import atnibam.space.system.mapper.AuthCredentialsMapper;
import atnibam.space.system.model.entity.AuthCredentials;
import atnibam.space.system.service.AuthCredentialsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Atnibam Aitay
 * @description 针对表【auth_credentials】的数据库操作Service实现
 * @createDate 2024-01-23 17:39:07
 */
@Service
public class AuthCredentialsServiceImpl extends ServiceImpl<AuthCredentialsMapper, AuthCredentials>
        implements AuthCredentialsService {

}