package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record FirmResultV1(
        UUID id,
        String name,
        String hubId
) {}
