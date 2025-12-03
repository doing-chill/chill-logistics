package chill_logistics.delivery_server.infrastructure.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteAfterCreateV1(
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
    Integer expectedDeliveryDuration
) {}
