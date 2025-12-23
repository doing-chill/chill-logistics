package chill_logistics.product_server.infrastructure.kafka.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockDecreaseCompletedV1(

//        UUID eventId,    // 멱등성 / Inbox용
        UUID orderId,
        UUID productId,
        int quantity,
        LocalDateTime occurredAt
) {}
