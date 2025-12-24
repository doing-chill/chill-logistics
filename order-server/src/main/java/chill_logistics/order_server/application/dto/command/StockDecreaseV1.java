package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record StockDecreaseV1(
        UUID orderId,
        UUID productId,
        int quantity
) {}
