package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record FirmInfoV1(
        UUID id,
        String name
) {
    public static FirmInfoV1 from(FirmResultV1 result) {
        return new FirmInfoV1(
                result.id(),
                result.name()
        );
    }
}
