package chill_logistics.delivery_server.infrastructure.user.dto.response;

import java.util.UUID;

public record AssignDeliveryAdminResponseDtoV1(
    UUID userId,
    String username
) {}
