package atnibam.space;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

@MapperScan("atnibam.space.user.mapper")
@SpringBootApplication
@EnableOpenApi
@EnableFeignClients
public class UserPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserPlatformApplication.class, args);
        System.out.println("\n启动成功：Sa-Token配置如下：" + SaManager.getConfig());
        System.out.println(SaManager.getConfig().getSign());
    }
}
