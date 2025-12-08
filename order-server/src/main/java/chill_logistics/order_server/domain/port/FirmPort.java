package chill_logistics.order_server.domain.port;

import chill_logistics.order_server.application.dto.command.FirmResultV1;

import java.util.UUID;

public interface FirmPort {

    FirmResultV1 readFirmById(UUID firmId, String firmType);

    UUID readHubId(UUID receiverFirmId);
}
