package space.atnibam.common.swagger.annotation;

import org.springframework.context.annotation.Import;
import space.atnibam.common.swagger.config.SwaggerAutoConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableCustomSwagger {
}