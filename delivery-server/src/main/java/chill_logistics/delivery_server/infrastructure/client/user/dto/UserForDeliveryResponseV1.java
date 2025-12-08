package chill_logistics.delivery_server.infrastructure.client.user.dto;

import java.util.UUID;

public record UserForDeliveryResponseV1(
    UUID userId,
    String name,
    String role
) {}
