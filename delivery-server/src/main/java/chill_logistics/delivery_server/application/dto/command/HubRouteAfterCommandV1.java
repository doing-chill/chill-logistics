package chill_logistics.delivery_server.application.dto.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteAfterCommandV1(
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

// TODO: expectedDeliveryDuration 타입 수정 필요한지 확인