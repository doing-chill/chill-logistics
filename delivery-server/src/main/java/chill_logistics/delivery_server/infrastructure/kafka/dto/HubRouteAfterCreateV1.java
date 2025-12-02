package chill_logistics.delivery_server.infrastructure.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteAfterCreateV1(
    UUID orderId,
    UUID startHubId,
    UUID endHubId,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt
    // + 예상 소요 시간 (허브 간 이동 정보 기반)
) {}
