package space.atnibam.common.swagger.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger 在 springboot 2.6.x 不兼容问题的处理
 */
public class SwaggerBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
            customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
        }
        return bean;
    }

    /**
     * 自定义 springfox 的 HandlerMapping 列表
     *
     * @param mappings HandlerMapping 列表
     */
    private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
        List<T> copy = mappings.stream().filter(mapping -> mapping.getPatternParser() == null)
                .collect(Collectors.toList());
        mappings.clear();
        mappings.addAll(copy);
    }

    /**
     * 获取 HandlerMapping 列表
     *
     * @param bean 要获取 HandlerMapping 列表的 bean 对象
     * @return HandlerMapping 列表
     */
    @SuppressWarnings("unchecked")
    private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
        try {
            Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
            field.setAccessible(true);
            return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}