package space.atnibam.common.swagger.annotation;

import space.atnibam.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableCustomSwagger {

}
