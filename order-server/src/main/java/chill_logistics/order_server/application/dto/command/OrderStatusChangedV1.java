package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.domain.entity.OrderStatus;
import java.util.UUID;

public record OrderStatusChangedV1(
    UUID orderId,
    OrderStatus orderStatus,
    java.time.LocalDateTime changedAt
) {}
