package chill_logistics.product_server.application.dto.command;

import chill_logistics.product_server.domain.entity.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductResultV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable,
        LocalDateTime createdAt
) {
    public static CreateProductResultV1 from(Product product) {
        return new CreateProductResultV1(
                product.getId(),
                product.getName(),
                product.getFirmId(),
                product.getHubId(),
                product.getStockQuantity(),
                product.getPrice(),
                product.getSellable(),
                product.getCreatedAt()
        );
    }
}
