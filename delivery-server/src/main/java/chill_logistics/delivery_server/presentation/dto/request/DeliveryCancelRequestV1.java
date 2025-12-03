package chill_logistics.delivery_server.presentation.dto.request;

import chill_logistics.delivery_server.application.DeliveryType;

public record DeliveryCancelRequestV1(
    DeliveryType deliveryType
) {}
