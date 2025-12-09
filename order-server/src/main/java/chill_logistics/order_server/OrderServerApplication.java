package chill_logistics.order_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
	scanBasePackages = {
		"chill_logistics.order_server",
		"lib"
	}
)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "chill_logistics.order_server.infrastructure")
public class OrderServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServerApplication.class, args);
	}

}
