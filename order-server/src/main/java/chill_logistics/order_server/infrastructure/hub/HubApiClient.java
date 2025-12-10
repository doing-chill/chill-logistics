package chill_logistics.order_server.infrastructure.hub;

import chill_logistics.order_server.domain.port.HubPort;
import chill_logistics.order_server.infrastructure.hub.dto.UserHubsResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubApiClient implements HubPort {

    private final HubFeignClient hubFeignClient;

    @Override
    public List<UUID> readHubId(UUID currentUserId) {
        return hubFeignClient.readUserHubs(currentUserId)
                .getData()
                .stream()
                .map(UserHubsResponseV1::managingHubId)
                .toList();
    }
}
