package chill_logistics.delivery_server.infrastructure.ai.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record AiDeadlineRequestV1(
    UUID orderId,
    UUID supplierHubId,
    String supplierHubName,
    String supplierHubFullAddress,
    UUID receiverHubId,
    String receiverHubName,
    String receiverHubFullAddress,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt,
    Integer expectedDeliveryDuration,
    String deliveryPersonName
) {}
