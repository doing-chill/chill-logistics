package chill_logistics.delivery_server.infrastructure.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {

        return Logger.Level.BASIC;
    }
}
