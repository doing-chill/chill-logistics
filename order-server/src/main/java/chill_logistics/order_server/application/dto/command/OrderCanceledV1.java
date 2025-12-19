package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.domain.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCanceledV1(
    UUID orderId,
    OrderStatus orderStatus,
    LocalDateTime changedAt
) {}
