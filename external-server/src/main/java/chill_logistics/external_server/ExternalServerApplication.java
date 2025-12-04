package chill_logistics.external_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.external_server",
        "lib"
    }
)
@EnableDiscoveryClient
public class ExternalServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalServerApplication.class, args);
    }
}
