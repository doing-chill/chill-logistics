package chill_logistics.order_server.application.dto.query;

import java.util.UUID;

public record ReceiverResultV1(
        UUID id,
        String name,
        String address
) {
    public static ReceiverResultV1 from(FirmQueryResultV1 result) {
        return new ReceiverResultV1(
                result.id(),
                result.name(),
                result.address()
        );
    }
}
