package atnibam.space.user.service.impl;

//import atnibam.space.api.system.RemoteMsgRecordService;
//import atnibam.space.common.core.constant.Constants;
import atnibam.space.common.core.constant.UserConstants;
//import atnibam.space.common.core.domain.LocalMessageRecord;
//import atnibam.space.common.core.domain.MqMessage;
import atnibam.space.common.core.domain.UserInfo;
import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.SystemServiceException;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.Base64FileReader;
import atnibam.space.common.core.utils.Base64ToMultipartFileUtils;
import atnibam.space.common.core.utils.DateUtils;
import atnibam.space.common.core.utils.RandomNameUtils;
import atnibam.space.common.minio.service.MinioSysFileService;
//import atnibam.space.common.rocketmq.constant.RocketMQConstants;
//import atnibam.space.common.rocketmq.service.MQProducerService;
import atnibam.space.user.mapper.UserInfoMapper;
import atnibam.space.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.apache.rocketmq.client.producer.SendCallback;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
//import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.support.TransactionSynchronization;
//import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.Objects;
//import java.util.UUID;

/**
 * 针对表【user_info】的数据库操作Service实现
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MinioSysFileService minioSysFileService;

//    @Autowired
//    private MQProducerService mqProducerService;
//
//    @Autowired
//    private RemoteMsgRecordService remoteMsgRecordService;
//
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfo queryUserInfo(String userId) {
        UserInfo userInfo = this.getById(userId);
        checkUserStatus(userInfo.getUserStatus().toString());
        return userInfo;
    }

    /**
     * 登出操作
     *
     * @param userId 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void logout(String userId) {
        // 将注销状态标记为确认注销,实际上没有注销
//        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<UserInfo>().eq(UserInfo::getUserId, userId).set(UserInfo::getLogoutStatus, UserConstants.LOGOUT);
//        this.update(updateWrapper);

        // 15天后进行注销逻辑
//        MqMessage mqMessage = new MqMessage(UUID.randomUUID().toString(), userId);
//        LocalMessageRecord messageRecord = mqProducerService.getMsgRecord(RocketMQConstants.DELAY_TOPIC, RocketMQConstants.LOGOUT_DELAY_TAG, mqMessage.getMessageBody(), Constants.USER_SERVICE, Constants.DELAY_LOGOUT);
//        messageRecord.setScheduledTime(DateUtils.addDaysToDate(DateUtils.getNowDate(), 15));
//        remoteMsgRecordService.saveMsgRecord(messageRecord);

        // 注册事务同步
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//                // 异步延迟发送
//                rocketMQTemplate.asyncSend(messageRecord.getTopic() + ":" + messageRecord.getTags(), MessageBuilder.withPayload(mqMessage).build(), new SendCallback() {
//                    @Override
//                    public void onSuccess(SendResult sendResult) {
//                        remoteMsgRecordService.updateMsgRecord(mqProducerService.asyncMsgRecordOnSuccessHandler(messageRecord, sendResult));
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//                        // log.error
//                        remoteMsgRecordService.updateMsgRecord(mqProducerService.asyncMsgRecordOnFailHandler(messageRecord));
//                    }
//                }, mqProducerService.messageTimeOut, 18);
//            }
//        });

    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfo userInfo) {
        checkUserStatus(userInfo.getUserStatus().toString());
        userInfo.setUserAvatar(setAvatarToRegistration(userInfo.getUserAvatar()));
        this.updateById(userInfo);
    }

    /**
     * 注册操作
     *
     * @param userInfo 用户信息
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registration(UserInfo userInfo) throws IOException {
        // 检查用户是否已注销，并返回用户的ID
        Integer userId = checkSignupFromLogout(userInfo);
        // 为用户设置随机名称
        userInfo.setUserName(setRandomName(userInfo.getUserName()));
        // 设置用户注册时间
        userInfo.setUserRegistTime(DateUtils.getNowDate());
        // 获取默认头像资源
        Resource resource = resourceLoader.getResource(UserConstants.DEFAULT_HEAD_PICTURE);
        // 将默认头像设置为用户的头像
        userInfo.setUserAvatar(setAvatarToRegistration(Base64FileReader.readBase64FromFile(resource.getFile().getPath())));
        // 如果用户ID为空，表示用户是新注册的用户
        if (Objects.isNull(userId)) {
            // 将用户信息保存到数据库中
            this.save(userInfo);
        } else {
            // 用户ID不为空，表示用户已存在
            // 更新用户信息到数据库中
            userInfo.setUserId(userId);
            this.updateById(userInfo);
        }
    }

    /**
     * 根据邮箱查询用户信息
     *
     * @param email   邮箱
     * @param appCode 应用编码
     * @return 用户信息
     */
    @Override
    public UserInfo queryUserInfoByEmail(String email, String appCode) {
        return userInfoMapper.queryUserInfoByEmail(email, appCode);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone   手机号
     * @param appCode 应用编码
     * @return 用户信息
     */
    @Override
    public UserInfo queryUserInfoByPhone(String phone, String appCode) {
        return userInfoMapper.queryUserInfoByPhone(phone, appCode);
    }

    /**
     * 设置随机名称
     * 如果传入的名称不为空，则直接返回传入的名称
     * 否则生成随机的中文名称并返回
     *
     * @param name 用户名
     * @return 用户名
     */
    private String setRandomName(String name) {
        if (name != null) {
            return name;
        }
        return RandomNameUtils.getRandomChineseCharacters();
    }

    /**
     * 将头像设置为注册时的头像
     *
     * @param avatarBase64 头像
     */
    public String setAvatarToRegistration(String avatarBase64) {
        // 将avatarBase64按逗号分隔成字符串数组base64Array
        final String[] base64Array = avatarBase64.split(",");

        // 定义字符串变量dataUir、data和url
        String dataUir, data, url;

        // 如果base64Array的长度大于1，则分别赋值给dataUir和data；否则根据base64代表的具体文件构建dataUir和data
        if (base64Array.length > 1) {
            dataUir = base64Array[0];
            data = base64Array[1];
        } else {
            // 根据base64代表的具体文件构建
            dataUir = UserConstants.AVATAR_FORMAT;
            data = base64Array[0];
        }

        // 创建Base64ToMultipartFileUtils对象multipartFile，传入data和dataUir作为参数
        Base64ToMultipartFileUtils multipartFile = new Base64ToMultipartFileUtils(data, dataUir);

        // 调用minioSysFileService的upload方法上传multipartFile，并将返回的URL赋值给url
        try {
            url = minioSysFileService.upload(multipartFile);
        } catch (Exception e) {
            // 打印错误日志并抛出系统服务异常
            log.error("MinIO获取URL服务异常");
            throw new SystemServiceException(ResultCode.INTERNAL_ERROR);
        }

        // 检查url是否为空字符串，若为空则打印错误日志并抛出系统服务异常
        if (!org.springframework.util.StringUtils.hasText(url)) {
            log.error("MinIO获取URL服务异常");
            throw new SystemServiceException(ResultCode.INTERNAL_ERROR);
        }

        // 返回上传成功后得到的URL
        return url;

    }

    /**
     * 检查用户状态
     * 如果用户状态为“用户已冻结”，则抛出系统服务异常
     * 如果用户状态为“已注销”，则抛出系统服务异常
     *
     * @param status 用户状态
     */
    private void checkUserStatus(String status) {
        switch (status) {
            case UserConstants.USER_DISABLE:
                throw new SystemServiceException(ResultCode.ACCOUNT_FREEZE);
            case UserConstants.LOGOUT:
                throw new SystemServiceException(ResultCode.ACCOUNT_LOGOUT);
        }
    }

    /**
     * 校验注册账号是否为注销的账号
     * 获取指定凭证ID的用户信息
     * 如果用户信息不为空并且用户状态不为“已注销”，则将用户信息恢复默认状态并抛出用户操作异常
     * 如果用户信息为空或者用户状态为“已注销”，则返回传入的用户ID
     *
     * @param userInfoDTO 用户信息
     * @return 用户ID
     */
    private Integer checkSignupFromLogout(UserInfo userInfoDTO) {
        UserInfo userInfo = this.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getCredentialsId, userInfoDTO.getCredentialsId()));
        if (!Objects.isNull(userInfo) && !userInfo.getUserStatus().toString().equals(UserConstants.LOGOUT)) {
            //账号已注销将信息恢复默认让用户重新重新登陆
            userInfoMapper.recoverDefaultInfo(userInfoDTO.getUserId());
            throw new UserOperateException(ResultCode.ACCOUNT_LOGOUT);
        }
        return userInfoDTO.getUserId();
    }

    /**
     * 根据凭证ID和应用编码查询用户信息
     *
     * @param credentialsId 凭证ID
     * @param appCode       应用编码
     * @return 用户信息
     */
    @Override
    public UserInfo queryUserInfoByCredentialsId(String credentialsId, String appCode) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getCredentialsId, credentialsId).eq(UserInfo::getAppCode, appCode);
        return this.getOne(queryWrapper);
    }

}
