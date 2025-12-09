package chill_logistics.delivery_server.application.dto.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HubRouteAfterCommandV1(
    UUID orderId,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt,
    int expectedDeliveryDuration,
    List<HubRouteHubInfoV1> pathHubs
) {}
