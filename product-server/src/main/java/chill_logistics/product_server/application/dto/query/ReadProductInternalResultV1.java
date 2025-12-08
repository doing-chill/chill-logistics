package chill_logistics.product_server.application.dto.query;

import chill_logistics.product_server.domain.entity.Product;

import java.util.UUID;

public record ReadProductInternalResultV1(
        UUID productId,
        String name,
        UUID firmId,
        int stockQuantity,
        int price
) {
    public static ReadProductInternalResultV1 from(Product product) {
        return new ReadProductInternalResultV1(
                product.getId(),
                product.getName(),
                product.getFirmId(),
                product.getStockQuantity(),
                product.getPrice()
        );
    }
}
