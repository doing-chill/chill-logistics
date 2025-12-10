package chill_logistics.user_server.application.dto.command;

import chill_logistics.user_server.domain.entity.DeliveryAdminType;

import java.util.UUID;

public record AssignDeliveryAdminCommandV1(
        UUID hubId,
        DeliveryAdminType deliveryAdminType
) { }
