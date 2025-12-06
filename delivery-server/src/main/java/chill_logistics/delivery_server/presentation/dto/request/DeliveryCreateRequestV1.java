package chill_logistics.delivery_server.presentation.dto.request;

import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeliveryCreateRequestV1(

    @NotNull
    HubRouteAfterCommandV1 orderInfo,

    @NotNull
    UUID hubDeliveryPersonId,

    @NotNull
    UUID firmDeliveryPersonId
) {}
