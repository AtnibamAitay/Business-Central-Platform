package atnibam.space.system.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSConfig {

    /**
     * 短信服务器密钥ID
     */
    @Value("${sms.accessKeyId}")
    private String accessKeyId;

    /**
     * 短信服务器密钥
     */
    @Value("${sms.secret}")
    private String secret;

    /**
     * 短信服务器区域
     */
    @Value("${sms.regionId}")
    private String regionId;

    /**
     * 配置静态凭证提供者
     *
     * @return 静态凭证提供者
     */
    @Bean
    public StaticCredentialProvider staticCredentialProvider() {
        return StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(secret)
                .build());
    }

    /**
     * 配置异步客户端
     *
     * @param provider 静态凭证提供者
     * @return 异步客户端
     */
    @Bean
    public AsyncClient asyncClient(StaticCredentialProvider provider) {
        return AsyncClient.builder()
                .region(regionId)
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
    }
}
