package chill_logistics.delivery_server.infrastructure.kafka.dto;

import chill_logistics.delivery_server.application.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderStatusChangedV1(
    UUID orderId,
    OrderStatus orderStatus,
    LocalDateTime changedAt
) {}
