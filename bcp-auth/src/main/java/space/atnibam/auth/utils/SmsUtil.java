package space.atnibam.auth.utils;

import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.SystemServiceException;
import atnibam.space.common.core.exception.UserOperateException;
import atnibam.space.common.core.utils.ValidatorUtil;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static atnibam.space.common.core.enums.ResultCode.PHONE_NUM_NON_COMPLIANCE;

@Slf4j
@Component
public class SmsUtil {

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateCode}")
    private String templateCode;

    @Autowired
    private AsyncClient client;

    /**
     * 校验手机号是否符合规范
     *
     * @param phoneNumber 手机号
     */
    public void isValidPhoneNumberFormat(String phoneNumber) {
        if (!ValidatorUtil.isMobile(phoneNumber)) {
            throw new UserOperateException(PHONE_NUM_NON_COMPLIANCE);
        }
    }

    /**
     * 发送短信
     *
     * @param phone   短信接收手机号
     * @param code    短信验证码
     * @param content 短信正文
     */
    public void sendMsg(String phone, String code, String content) {
        // 创建发送短信请求对象
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumbers(phone)
                .templateParam(content + "{  \"code\":\"" + code + "\"}")
                .build();

        // 发送短信请求并获取响应结果
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        try {
            log.info("阿里云短信响应信息：" + response.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new SystemServiceException(ResultCode.ACCOUNT_LOGOUT);
        }
    }
}
