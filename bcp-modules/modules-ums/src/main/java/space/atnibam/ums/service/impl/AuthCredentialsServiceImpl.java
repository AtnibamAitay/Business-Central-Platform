package space.atnibam.ums.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import space.atnibam.common.core.domain.AuthCredentials;
import space.atnibam.common.core.domain.dto.BindingCertificateDTO;
import space.atnibam.common.core.exception.UserOperateException;
import space.atnibam.common.redis.service.RedisCache;
import space.atnibam.ums.mapper.AuthCredentialsMapper;
import space.atnibam.ums.service.AuthCredentialsService;

import javax.annotation.Resource;
import java.util.Objects;

import static space.atnibam.common.core.enums.ResultCode.ACCOUNT_EXIST;
import static space.atnibam.common.core.enums.ResultCode.USER_VERIFY_ERROR;
import static space.atnibam.common.redis.constant.CacheConstants.*;

/**
 * 针对表【auth_credentials】的数据库操作Service实现
 */
@Service
public class AuthCredentialsServiceImpl extends ServiceImpl<AuthCredentialsMapper, AuthCredentials>
        implements AuthCredentialsService {
    @Resource
    private RedisCache redisCache;

    /**
     * 检查手机号是否存在
     *
     * @param phoneNumber 手机号
     * @return AuthCredentials对象
     */
    @Override
    public AuthCredentials checkPhoneNumbExit(String phoneNumber) {
        LambdaQueryWrapper<AuthCredentials> queryWrapper = new LambdaQueryWrapper<AuthCredentials>().eq(AuthCredentials::getPhoneNumber, phoneNumber);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据邮箱获取用户账号数据
     *
     * @param email 邮箱
     * @return AuthCredentials 用户账号数据
     */
    @Override
    public AuthCredentials queryAuthCredentialsByEmail(String email) {
        LambdaQueryWrapper<AuthCredentials> queryWrapper = new LambdaQueryWrapper<AuthCredentials>().eq(AuthCredentials::getEmail, email);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据手机号查询AuthCredentials对象
     *
     * @param phone 手机号
     * @return AuthCredentials对象
     */
    @Override
    public AuthCredentials queryAuthCredentialsByPhone(String phone) {
        LambdaQueryWrapper<AuthCredentials> queryWrapper = new LambdaQueryWrapper<AuthCredentials>().eq(AuthCredentials::getPhoneNumber, phone);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据证书创建用户AuthCredentials对象
     *
     * @param certificate 证书
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserCredentialsByPhone(String certificate) {
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setPhoneNumber(certificate);
        this.save(authCredentials);
    }

    /**
     * 通过邮箱创建用户
     *
     * @param certificate 邮箱
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserCredentialsByEmail(String certificate) {
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setEmail(certificate);
        this.save(authCredentials);
    }

    /**
     * 绑定手机号
     *
     * @param bindingCertificateDTO 绑定证书DTO对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindingPhoneById(BindingCertificateDTO bindingCertificateDTO) {
        // 获取绑定证书DTO中的证书信息
        String certificate = bindingCertificateDTO.getCertificate();

        // 若通过手机号查询到的AuthCredentials对象不为空，则抛出账号已存在的用户操作异常
        if (!Objects.isNull(queryAuthCredentialsByPhone(certificate))) {
            throw new UserOperateException(ACCOUNT_EXIST);
        }

        // 从缓存中获取与当前证书关联的绑定码
        String code = redisCache.getCacheObject(BINDING_CODE_KEY + PHONE_KEY + bindingCertificateDTO.getCertificate());

        // 验证输入的验证码是否与缓存中的绑定码一致
        checkVerifyCodeCode(code, bindingCertificateDTO.getVerifyCode());

        // 获取账号ID
        Integer credentialsId = bindingCertificateDTO.getCredentialsId();

        // 创建LambdaUpdateWrapper对象，用于更新账号表中凭证ID对应的数据，将手机号设置为当前证书
        LambdaUpdateWrapper<AuthCredentials> updateWrapper = new LambdaUpdateWrapper<AuthCredentials>()
                .eq(AuthCredentials::getCredentialsId, credentialsId)
                .set(AuthCredentials::getPhoneNumber, certificate);

        // 执行数据更新操作
        this.update(updateWrapper);
    }

    /**
     * 绑定邮箱
     *
     * @param bindingCertificateDTO 绑定证书DTO对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindingEmailById(BindingCertificateDTO bindingCertificateDTO) {
        // 获取账号
        String certificate = bindingCertificateDTO.getCertificate();
        // 如果查到账号已经与邮箱绑定则抛出异常
        if (!Objects.isNull(queryAuthCredentialsByEmail(certificate))) {
            throw new UserOperateException(ACCOUNT_EXIST);
        }
        // 获取缓存中的绑定验证码
        String code = getCodeFromRedis(certificate, bindingCertificateDTO.getAppId());
        // 验证缓存中的绑定验证码和用户传入的验证码是否匹配
        checkVerifyCodeCode(code, bindingCertificateDTO.getVerifyCode());
        // 获取账号ID
        Integer credentialsId = bindingCertificateDTO.getCredentialsId();
        // 创建LambdaUpdateWrapper对象，用于更新账号的邮箱
        LambdaUpdateWrapper<AuthCredentials> updateWrapper = new LambdaUpdateWrapper<AuthCredentials>().eq(AuthCredentials::getCredentialsId, credentialsId).set(AuthCredentials::getEmail, certificate);
        // 更新账号的邮箱
        this.update(updateWrapper);
    }

    /**
     * 验证码验证
     *
     * @param code       验证码
     * @param verifyCode 输入的验证码
     */
    private void checkVerifyCodeCode(String code, String verifyCode) {
        if (!StringUtils.hasText(code) || !verifyCode.equals(code)) {
            throw new UserOperateException(USER_VERIFY_ERROR);
        }
    }

    /**
     * 从Redis中获取邮箱的验证码
     *
     * @param email 邮箱
     * @return 验证码
     * @throws UserOperateException 用户操作异常
     */
    private String getCodeFromRedis(String email, String appId) {
        // TODO:写成一个通用的方法，auth 也需要调用这个
        JSONObject cacheResult = redisCache.getCacheObject(BINDING_CODE_KEY + EMAIL_KEY + email);
        // 检查结果是否存在且数据部分不为空
        if (cacheResult != null && !cacheResult.isEmpty()) {
            // 获取结果中的数据部分并转换为JSONObject对象
            JSONObject dataObject = cacheResult.getJSONObject("data");
            // 检查数据部分是否为空
            if (dataObject != null && dataObject.getString("code") != null) {
                // 获取数据部分中的appId字段值
                String appIdCache = dataObject.getString("appId");
                // 检查appId字段值是否与传入的appId相等
                if (!appId.equals(appIdCache)) {
                    throw new UserOperateException(USER_VERIFY_ERROR);
                }
                // 获取数据部分中的code字段值
                String code = dataObject.getString("code");
                // 检查code字段值是否包含文本
                if (space.atnibam.common.core.utils.StringUtils.hasText(code)) {
                    return code;
                }
            }
        }

        //todo log
        throw new UserOperateException(USER_VERIFY_ERROR);
    }
}
