package chill_logistics.order_server.infrastructure.firm.dto;

import java.util.UUID;

public record FirmResponseV1(
        UUID id,
        String name,
        UUID hubId,
        String firmFullAddress,
        String firmOwnerName
) {}
