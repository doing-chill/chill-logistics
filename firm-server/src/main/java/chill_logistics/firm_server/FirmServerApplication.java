package chill_logistics.firm_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.firm_server",
        "lib"
    }
)
@EnableDiscoveryClient
public class FirmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirmServerApplication.class, args);
    }

}
