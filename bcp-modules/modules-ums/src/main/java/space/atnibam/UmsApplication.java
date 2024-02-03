package space.atnibam;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import space.atnibam.common.security.config.SecurityRedisConfig;
import space.atnibam.common.security.handler.GlobalExceptionHandler;
import space.atnibam.common.swagger.annotation.EnableCustomSwagger;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@ComponentScan(basePackages = {"space.atnibam.**"})
@MapperScan("space.atnibam.**.mapper")
@EnableOpenApi
@EnableFeignClients
@EnableCustomSwagger
@Import({SecurityRedisConfig.class, GlobalExceptionHandler.class})
public class UmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
        System.out.println("\n启动成功：Sa-Token配置如下：" + SaManager.getConfig());
        System.out.println(SaManager.getConfig().getSign());
    }
}
