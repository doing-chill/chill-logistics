package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.infrastructure.firm.dto.FirmResponseV1;

import java.util.UUID;

public record FirmResultV1(
        UUID id,
        String name,
        UUID hubId,
        String firmFullAddress,
        String firmOwnerName
) {
    public static FirmResultV1 from(FirmResponseV1 response) {
        return new FirmResultV1(
                response.id(),
                response.name(),
                response.hubId(),
                response.firmFullAddress(),
                response.firmOwnerName()
        );
    }
}
