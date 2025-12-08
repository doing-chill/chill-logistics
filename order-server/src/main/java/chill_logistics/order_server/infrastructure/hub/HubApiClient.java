package chill_logistics.order_server.infrastructure.hub;

import chill_logistics.order_server.domain.port.HubPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubApiClient implements HubPort {

    private final HubFeignClient hubFeignClient;

    @Override
    public UUID readHubId(UUID currentUserId) {
        return null;
    }
}
