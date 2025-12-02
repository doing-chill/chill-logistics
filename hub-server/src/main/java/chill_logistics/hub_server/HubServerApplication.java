package chill_logistics.hub_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.hub_server",
        "lib.entity",
        "lib.web",
        "lib.util"
    }
)
@EnableDiscoveryClient
@EnableJpaAuditing
@EnableFeignClients(basePackages = "chill_logistics.hub_server.infrastructure")
public class HubServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubServerApplication.class, args);
    }
}
