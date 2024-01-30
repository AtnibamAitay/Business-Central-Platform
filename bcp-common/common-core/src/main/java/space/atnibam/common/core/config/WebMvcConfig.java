package space.atnibam.common.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static space.atnibam.common.core.constant.ConfigConstants.*;

/**
 * @ClassName: WebMvcConfig
 * @Description: WebMvc配置类，主要进行资源处理和跨域请求的配置
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-12 19:11
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源的处理
     *
     * @param registry 资源处理器注册对象
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 定义资源处理路径，这里定义了/static/**的路径下的资源文件需要被处理
        // 实际映射到项目的classpath:/static/目录下
        registry.addResourceHandler(STATIC_RESOURCE_HANDLER).addResourceLocations(STATIC_RESOURCE_LOCATION);
    }

    /**
     * 创建一个MultipartResolver的Bean
     *
     * @return 返回StandardServletMultipartResolver实例
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    /**
     * 配置跨域访问控制
     *
     * @param registry 跨域注册对象
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对所有路径的请求都允许跨域访问
        registry.addMapping(CORS_PATH_PATTERN)
                // 允许所有来源进行跨域访问
                .allowedOriginPatterns(CORS_ALLOWED_ORIGIN_PATTERN)
                // 允许所有的请求方法进行跨域访问
                .allowedMethods(CORS_ALLOWED_METHODS)
                // 允许所有的请求header进行跨域访问
                .allowedHeaders(CORS_ALLOWED_HEADERS)
                // 允许发送cookie
                .allowCredentials(true)
                // 设置预检请求的缓存时间为3600秒
                .maxAge(CORS_MAX_AGE);
    }

    /**
     * 配置CORS过滤器
     *
     * @return 返回配置好的FilterRegistrationBean对象
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern(CORS_ALLOWED_ORIGIN_PATTERN);
        config.addAllowedHeader(CORS_ALLOWED_HEADERS);
        config.addAllowedMethod(CORS_ALLOWED_METHODS);
        // 将CORS配置注册到UrlBasedCorsConfigurationSource对象中
        source.registerCorsConfiguration(CORS_PATH_PATTERN, config);
        // 创建一个CORS过滤器，然后设置其排序值为最高（优先级最高）
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}