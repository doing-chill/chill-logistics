package chill_logistics.product_server.presentation.dto.response;

import chill_logistics.product_server.application.dto.CreateProductResultV1;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProductResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable,
        LocalDateTime createdAt
) {
    public static CreateProductResponseV1 from(CreateProductResultV1 result) {
        return new CreateProductResponseV1(
                result.id(),
                result.name(),
                result.firmId(),
                result.hubId(),
                result.stockQuantity(),
                result.price(),
                result.sellable(),
                result.createdAt()
        );
    }
}
