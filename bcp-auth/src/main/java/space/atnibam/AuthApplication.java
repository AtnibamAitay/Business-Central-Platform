package space.atnibam;

import space.atnibam.common.security.config.SecurityRedisConfig;
import space.atnibam.common.security.handler.GlobalExceptionHandler;
import space.atnibam.common.swagger.annotation.EnableCustomSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("space.atnibam.**.mapper")
@EnableScheduling
@EnableAsync
@EnableFeignClients
@EnableCustomSwagger
@Import({SecurityRedisConfig.class, GlobalExceptionHandler.class})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
