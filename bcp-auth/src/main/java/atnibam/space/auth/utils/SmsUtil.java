package atnibam.space.auth.utils;

import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.SystemServiceException;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
