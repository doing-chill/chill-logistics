package chill_logistics.order_server.infrastructure.hub;

import chill_logistics.order_server.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-server", configuration = FeignConfig.class)
public interface HubFeignClient {
}
