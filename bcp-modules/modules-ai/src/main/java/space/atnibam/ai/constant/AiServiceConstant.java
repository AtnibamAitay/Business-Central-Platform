package space.atnibam.ai.constant;

/**
 * @ClassName: AiServiceConstant
 * @Description:
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-18 16:41
 **/
public class AiServiceConstant {
    /**
     * JSON内容类型头信息。
     */
    public static final String JSON_CONTENT_TYPE = "application/json";

    /**
     * HTTP成功状态码。
     */
    public static final int HTTP_SUCCESS_STATUS = 200;

    /**
     * HTTP请求授权头字段名。
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 授权头Bearer前缀。
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * 请求参数中模型键名。
     */
    public static final String MODEL_KEY = "model";

    /**
     * 请求参数中消息键名。
     */
    public static final String MESSAGES_KEY = "messages";

    /**
     * JSON响应中选项键名。
     */
    public static final String CHOICES_KEY = "choices";

    /**
     * JSON响应中消息键名。
     */
    public static final String MESSAGE_KEY = "message";

    /**
     * JSON响应中内容键名。
     */
    public static final String CONTENT_KEY = "content";

    /**
     * 消息发送者角色。
     */
    public static final String USER_ROLE = "user";

    /**
     * 输入提示占位符。
     */
    public static final String INPUT_PLACEHOLDER = "{{input}}";

    /**
     * 内容类型头字段名。
     */
    public static final String CONTENT_TYPE = "Content-Type";
}
