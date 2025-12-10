package chill_logistics.order_server.domain.port;

import java.util.List;
import java.util.UUID;

public interface HubPort {
    List<UUID> readHubId(UUID currentUserId);
}
