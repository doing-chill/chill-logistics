package chill_logistics.delivery_server.infrastructure.user.dto.request;

import chill_logistics.delivery_server.infrastructure.user.DeliveryAdminType;
import java.util.UUID;

public record AssignDeliveryAdminRequestDtoV1(
    UUID hubId,
    String deliveryAdminType
) {}
