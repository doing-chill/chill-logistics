package chill_logistics.order_server.application.dto.query;

import chill_logistics.order_server.application.dto.command.FirmResultV1;

import java.util.UUID;

public record FirmQueryResultV1(
        UUID id,
        String name,
        String address
) {
    public static FirmQueryResultV1 from(FirmResultV1 result) {
        return new FirmQueryResultV1(
                result.id(),
                result.name(),
                result.firmFullAddress()
        );
    }
}
