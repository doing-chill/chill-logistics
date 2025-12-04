package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.query.ReceiverResultV1;

import java.util.UUID;

public record ReceiverResponseV1(
        UUID id,
        String name,
        String address
) {
    public static ReceiverResponseV1 from(ReceiverResultV1 result) {
        return new ReceiverResponseV1(
                result.id(),
                result.name(),
                result.address()
        );
    }
}
