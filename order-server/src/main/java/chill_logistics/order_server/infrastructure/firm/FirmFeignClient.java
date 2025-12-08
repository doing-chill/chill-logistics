package chill_logistics.order_server.infrastructure.firm;

import chill_logistics.order_server.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "firm-server", configuration = FeignConfig.class)
public interface FirmFeignClient {
}
