package chill_logistics.delivery_server.infrastructure.kafka.dto;

import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAfterCreateV1(
    UUID orderId,
    UUID startHubId,
    String startHubFullAddress,
    UUID endHubId,
    String endHubFullAddress,
    @Positive Integer deliverySequenceNum,
    String deliveryStatus,
    @Positive Double expectedDistance,
    LocalDateTime expectedDeliveryDuration
) {}
