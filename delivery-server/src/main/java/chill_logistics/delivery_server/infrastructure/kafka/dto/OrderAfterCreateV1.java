package chill_logistics.delivery_server.infrastructure.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAfterCreateV1(
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
) {}
