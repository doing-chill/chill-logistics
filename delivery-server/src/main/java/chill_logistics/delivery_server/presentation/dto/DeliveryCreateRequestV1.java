package chill_logistics.delivery_server.presentation.dto;

import chill_logistics.delivery_server.infrastructure.kafka.dto.HubRouteAfterCreateV1;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeliveryCreateRequestV1(

    @NotNull
    HubRouteAfterCreateV1 orderInfo,

    @NotNull
    UUID hubDeliveryPersonId,

    @NotNull
    UUID firmDeliveryPersonId
) {}
