package chill_logistics.delivery_server.infrastructure.client;

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
    chill_logistics.delivery_server.infrastructure.client.dto.HubForDeliveryResponseV1 getHub(@PathVariable("hubId") UUID hubId);

    record HubForDeliveryResponseV1(UUID hubId, String name) {}
}

// TODO: 수정 필요