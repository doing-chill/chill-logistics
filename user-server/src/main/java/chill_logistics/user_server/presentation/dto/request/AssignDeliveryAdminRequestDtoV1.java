package chill_logistics.user_server.presentation.dto.request;


import chill_logistics.user_server.application.dto.command.AssignDeliveryAdminCommandV1;
import chill_logistics.user_server.domain.entity.DeliveryAdminType;

import java.util.UUID;

public record AssignDeliveryAdminRequestDtoV1(
        UUID hubId,
        DeliveryAdminType deliveryAdminType
) {
    public AssignDeliveryAdminCommandV1 toCommand() {
        return new AssignDeliveryAdminCommandV1(
                this.hubId,
                this.deliveryAdminType
        );
    }
}
