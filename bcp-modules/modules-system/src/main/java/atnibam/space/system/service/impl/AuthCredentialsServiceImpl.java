package atnibam.space.system.service.impl;

import atnibam.space.system.exception.UserLoginException;
import atnibam.space.system.mapper.AuthCredentialsMapper;
import atnibam.space.system.model.entity.AuthCredentials;
import atnibam.space.system.service.AuthCredentialsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static atnibam.space.common.core.enums.ResultCode.USER_NOT_EXIST_BY_CODE;

/**
 * @author Atnibam Aitay
 * @description 针对表【auth_credentials】的数据库操作Service实现
 * @createDate 2024-01-23 17:39:07
 */
@Service
public class AuthCredentialsServiceImpl extends ServiceImpl<AuthCredentialsMapper, AuthCredentials>
        implements AuthCredentialsService {
    @Resource
    private AuthCredentialsMapper authCredentialsMapper;

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     */
    @Override
    public void checkEmailExist(String email) {
        if (!authCredentialsMapper.checkEmailExist(email)) {
            throw new UserLoginException(USER_NOT_EXIST_BY_CODE);
        }
    }

    /**
     * 检查手机号是否存在
     *
     * @param phoneNumber 手机号
     */
    @Override
    public void checkPhoneExist(String phoneNumber) {
        if (authCredentialsMapper.checkPhoneExist(phoneNumber)) {
            throw new UserLoginException(USER_NOT_EXIST_BY_CODE);
        }
    }
}