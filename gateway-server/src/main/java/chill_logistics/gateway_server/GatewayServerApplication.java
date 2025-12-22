package chill_logistics.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication(
	scanBasePackages = {
		"chill_logistics.gateway_server",
		"lib.jwt","lib.passport","lib.security"
	}, exclude = { ReactiveSecurityAutoConfiguration.class }
)
public class GatewayServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}
}


