package chill_logistics.order_server.infrastructure.product.dto;

import java.util.UUID;

public record ProductResponseV1(
        UUID productId,
        String name,
        UUID firmId,
        int stockQuantity,
        int price
) {}
