package chill_logistics.product_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
    scanBasePackages = {
        "chill_logistics.product_server",
        "lib.entity",
        "lib.web",
        "lib.util"
    }
)
@EnableDiscoveryClient
public class ProductServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServerApplication.class, args);
    }

}
