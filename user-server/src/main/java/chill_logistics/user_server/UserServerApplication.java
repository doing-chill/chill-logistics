package chill_logistics.user_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.user_server",
        "lib.entity",
        "lib.web",
        "lib.util",
        "lib.jwt"
    }
)
@EnableJpaAuditing
@EnableDiscoveryClient
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
