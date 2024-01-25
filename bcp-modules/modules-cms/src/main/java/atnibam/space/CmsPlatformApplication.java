package atnibam.space;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("atnibam.space.cms.mapper")
public class CmsPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsPlatformApplication.class, args);
    }
}
