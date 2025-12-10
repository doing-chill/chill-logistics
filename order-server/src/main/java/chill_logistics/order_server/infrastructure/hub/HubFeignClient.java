package chill_logistics.order_server.infrastructure.hub;

import chill_logistics.order_server.infrastructure.config.FeignConfig;
import chill_logistics.order_server.infrastructure.hub.dto.UserHubsResponseV1;
import lib.web.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub-server", configuration = FeignConfig.class)
public interface HubFeignClient {

    @GetMapping("/v1/hubs/userHubs/{userId}")
    BaseResponse<List<UserHubsResponseV1>> readUserHubs(@PathVariable UUID userId);
}
