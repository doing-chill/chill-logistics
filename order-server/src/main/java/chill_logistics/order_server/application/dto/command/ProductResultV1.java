package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.infrastructure.product.dto.ProductResponseV1;

import java.util.UUID;

public record ProductResultV1(
        UUID productId,
        String name,
        UUID firmId,
        int stockQuantity,
        int price
) {
    public static ProductResultV1 from(ProductResponseV1 response) {
        return new ProductResultV1(
                response.productId(),
                response.name(),
                response.firmId(),
                response.stockQuantity(),
                response.price()
        );
    }
}
