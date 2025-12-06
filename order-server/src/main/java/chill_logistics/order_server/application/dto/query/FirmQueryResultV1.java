package chill_logistics.order_server.application.dto.query;

import java.util.UUID;

public record FirmQueryResultV1(
        UUID id,
        String name,
        String address
) {}
