package chill_logistics.product_server.infrastructure.kafka.dto;

import java.util.UUID;

public record StockDecreaseV1(
        UUID orderId,
        UUID productId,
        int quantity
) {}
