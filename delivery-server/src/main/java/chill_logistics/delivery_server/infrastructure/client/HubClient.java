package chill_logistics.delivery_server.infrastructure.client;

import chill_logistics.delivery_server.infrastructure.client.dto.HubForDeliveryResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "hubClient",
    url = "${clients.hub.url}",
    configuration = chill_logistics.delivery_server.infrastructure.config.FeignConfig.class
)
public interface HubClient {

    @GetMapping("/v1/internal/hubs/{hubId}")
    HubForDeliveryResponseV1 getHub(@PathVariable("hubId") UUID hubId);
}

// TODO: 수정 필요