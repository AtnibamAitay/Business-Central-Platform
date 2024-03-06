package space.atnibam.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: OpenAIConfig
 * @Description: OpenAI的配置类，用来获取配置文件中关于OpenAI的参数
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-12 20:09
 **/
@Configuration
@ConfigurationProperties(prefix = "openai")
@Data
public class OpenAIConfig {

    /**
     * OpenAI的基础URL
     */
    private String baseUrl;

    /**
     * OpenAI的API密钥
     */
    private String apiKey;
}