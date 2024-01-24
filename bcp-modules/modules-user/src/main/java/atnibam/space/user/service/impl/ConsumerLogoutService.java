package atnibam.space.user.service.impl;

import atnibam.space.common.core.constant.UserConstants;
import atnibam.space.common.core.domain.MqMessage;
import atnibam.space.common.core.domain.UserInfo;
//import atnibam.space.common.rocketmq.constant.RocketMQConstants;
import atnibam.space.common.service.aop.anno.MQIdempotency;
import atnibam.space.user.service.UserInfoService;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 延迟进行账号注销操作
 */
//@Component
//@RocketMQMessageListener(topic = RocketMQConstants.DELAY_TOPIC, selectorExpression = RocketMQConstants.LOGOUT_DELAY_TAG, consumerGroup = "Group1")
//public class ConsumerLogoutService implements RocketMQListener<MqMessage> {
//    @Autowired
//    private UserInfoService userInfoService;
//
//    @Override
//    @MQIdempotency
//    public void onMessage(MqMessage mqMessage) {
//        // 查询用户信息
//        UserInfo userInfo = userInfoService.queryUserInfo(mqMessage.getMessageBody());
//
//        if (Objects.isNull(userInfo)) {
//            // 如果用户信息为空，则日志记录并返回
//            //todo log
//            return;
//        }
//
//        // 如果用户取消了注销或者注销状态为“正常”或“取消注销”，则直接返回
//        if (UserConstants.NORMAL.equals(userInfo.getLogoutStatus().toString()) || UserConstants.CANCEL_LOGOUT.equals(userInfo.getLogoutStatus().toString())) {
//            return;
//        }
//
//        // 更新用户状态为“注销”
//        userInfo.setUserStatus(Integer.valueOf(UserConstants.LOGOUT));
//        userInfoService.updateById(userInfo);
//    }
//}
