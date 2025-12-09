package chill_logistics.firm_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.firm_server",
        "lib"
    }
)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "chill_logistics.firm_server.infrastructure")
public class FirmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirmServerApplication.class, args);
    }

}
