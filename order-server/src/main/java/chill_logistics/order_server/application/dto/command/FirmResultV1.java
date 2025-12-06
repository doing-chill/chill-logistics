package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record FirmResultV1(
        UUID id,
        String name,
        UUID hubId,
        String firmFullAddress,  // 수령업체일때만 받음
        String firmOwnerName     // 수령업체일때만 받음
) {}
