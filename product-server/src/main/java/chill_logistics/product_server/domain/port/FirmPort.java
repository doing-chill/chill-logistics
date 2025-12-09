package chill_logistics.product_server.domain.port;

import java.util.UUID;

public interface FirmPort {
    void validateExists(UUID firmId);
}
