package chill_logistics.product_server.presentation.dto.response;

import chill_logistics.product_server.application.dto.query.ReadProductDetailResultV1;

import java.util.UUID;

public record ReadProductDetailResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) {
    public static ReadProductDetailResponseV1 from(ReadProductDetailResultV1 result) {
        return new ReadProductDetailResponseV1(
                result.id(),
                result.name(),
                result.firmId(),
                result.hubId(),
                result.stockQuantity(),
                result.price(),
                result.sellable()
        );
    }
}
