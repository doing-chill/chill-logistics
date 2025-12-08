package chill_logistics.hub_server.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteResultResponse (
    UUID orderId,
    UUID startHubId,
    String startHubName,
    String startHubFullAddress,
    UUID endHubId,
    String endHubName,
    String endHubFullAddress,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt,
    int expectedDeliveryDuration
){}