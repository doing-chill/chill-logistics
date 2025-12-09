package chill_logistics.product_server.infrastructure.Firm;

import chill_logistics.product_server.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "firm-server", configuration = FeignConfig.class)
public interface FirmFeignClient {
}
