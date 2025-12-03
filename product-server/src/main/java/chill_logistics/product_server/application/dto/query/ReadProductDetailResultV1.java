package chill_logistics.product_server.application.dto.query;

import chill_logistics.product_server.domain.entity.Product;

import java.util.UUID;

public record ReadProductDetailResultV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) {
    public static ReadProductDetailResultV1 from(Product product) {
        return new ReadProductDetailResultV1(
                product.getId(),
                product.getName(),
                product.getFirmId(),
                product.getHubId(),
                product.getStockQuantity(),
                product.getPrice(),
                product.getSellable()
        );
    }
}
