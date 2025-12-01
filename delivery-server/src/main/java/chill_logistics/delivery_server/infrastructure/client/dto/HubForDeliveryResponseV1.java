package chill_logistics.delivery_server.infrastructure.client.dto;

import java.util.UUID;

public record HubForDeliveryResponseV1(
    UUID hubId,
    String hubName
) {}