package chill_logistics.order_server.domain.port;

import java.util.UUID;

public interface FirmPort {
    UUID readHubId(UUID receiverFirmId);
}
