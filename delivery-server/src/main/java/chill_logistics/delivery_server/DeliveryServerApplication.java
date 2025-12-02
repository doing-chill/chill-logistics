package chill_logistics.delivery_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
	scanBasePackages = {
		"chill_logistics.delivery_server",
		"lib.entity",
		"lib.web",
		"lib.util"
	}
)
@EnableDiscoveryClient
@EnableFeignClients
public class DeliveryServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(DeliveryServerApplication.class, args);
	}
}
