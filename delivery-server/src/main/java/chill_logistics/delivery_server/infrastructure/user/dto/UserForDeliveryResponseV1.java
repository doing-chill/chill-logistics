package chill_logistics.delivery_server.infrastructure.user.dto;

import java.util.UUID;

public record UserForDeliveryResponseV1(
    UUID userId,
    String userName,
    String role
) {}
