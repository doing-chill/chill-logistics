package chill_logistics.user_server.domain.port;

import java.util.UUID;

public interface HubPort {

    String readHubName(UUID hubId);
}
