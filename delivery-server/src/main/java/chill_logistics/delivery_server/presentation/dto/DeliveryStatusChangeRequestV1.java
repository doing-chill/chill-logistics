package chill_logistics.delivery_server.presentation.dto;

import chill_logistics.delivery_server.application.DeliveryType;
import chill_logistics.delivery_server.domain.entity.DeliveryStatus;

public record DeliveryStatusChangeRequestV1(

    DeliveryType deliveryType,
    DeliveryStatus nextDeliveryStatus
) {}
