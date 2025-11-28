package chill_logistics.hub_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.hub_server",
        "lib.entity",
        "lib.web",
        "lib.util"
    }
)
@EnableDiscoveryClient
public class HubServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubServerApplication.class, args);
    }
}
