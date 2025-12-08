package chill_logistics.delivery_server.application.dto.command;

import java.util.UUID;

public record AssignedDeliveryPersonV1(
    UUID userId,
    String userName
) {}
