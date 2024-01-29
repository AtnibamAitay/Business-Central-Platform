package space.atnibam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import space.atnibam.common.swagger.annotation.EnableCustomSwagger;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("space.atnibam.**.mapper")
@EnableCustomSwagger
public class PmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class, args);
    }
}

