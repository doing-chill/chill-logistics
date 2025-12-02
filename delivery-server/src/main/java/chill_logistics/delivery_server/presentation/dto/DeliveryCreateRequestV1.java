package chill_logistics.delivery_server.presentation.dto;

import chill_logistics.delivery_server.infrastructure.kafka.dto.OrderAfterCreateV1;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeliveryCreateRequestV1(

    @NotNull
    OrderAfterCreateV1 orderInfo,

    @NotNull
    UUID hubDeliveryPersonId,

    @NotNull
    UUID firmDeliveryPersonId
) {}
