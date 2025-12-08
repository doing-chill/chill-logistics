package chill_logistics.order_server.domain.port;

import java.util.UUID;

public interface HubPort {
    UUID readHubId(UUID currentUserId);
}
