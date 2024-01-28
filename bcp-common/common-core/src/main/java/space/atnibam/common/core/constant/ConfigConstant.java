package space.atnibam.common.core.constant;

/**
 * @ClassName: ConfigConstant
 * @Description: Swagger配置常量类
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-12 19:25
 **/
public class ConfigConstant {

    //=============================跨域请求配置常量=============================

    /**
     * 用于静态资源处理器的路径模式。
     */
    public static final String STATIC_RESOURCE_HANDLER = "/static/**";

    /**
     * 静态资源实际位置的类路径。
     */
    public static final String STATIC_RESOURCE_LOCATION = "classpath:/static/";

    /**
     * 表示所有路径的通配符。
     */
    public static final String CORS_PATH_PATTERN = "/**";

    /**
     * 表示允许所有来源的通配符。
     */
    public static final String CORS_ALLOWED_ORIGIN_PATTERN = "*";

    /**
     * 表示允许所有请求方法的通配符。
     */
    public static final String CORS_ALLOWED_METHODS = "*";

    /**
     * 表示允许所有请求头的通配符。
     */
    public static final String CORS_ALLOWED_HEADERS = "*";

    /**
     * 预检请求的缓存时间（单位：秒）。
     */
    public static final long CORS_MAX_AGE = 3600;
}
