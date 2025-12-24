package chill_logistics.order_server.infrastructure.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockDecreaseCompletedV1(

        UUID orderId,
        UUID productId,
        int quantity,
        LocalDateTime occurredAt
) {}
