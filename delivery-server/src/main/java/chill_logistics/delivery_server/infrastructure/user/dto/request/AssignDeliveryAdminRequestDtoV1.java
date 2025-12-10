package chill_logistics.delivery_server.infrastructure.user.dto.request;

import java.util.UUID;

public record AssignDeliveryAdminRequestDtoV1(
    UUID hubId,
    String deliveryAdminType
) {}
