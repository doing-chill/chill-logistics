package chill_logistics.product_server.presentation.dto.response;

import chill_logistics.product_server.application.dto.query.ReadProductInternalResultV1;

import java.util.UUID;

public record ReadProductInternalResponseV1(
        UUID productId,
        String name,
        UUID firmId,
        int stockQuantity,
        int price
) {
    public static ReadProductInternalResponseV1 from(ReadProductInternalResultV1 result) {
        return new ReadProductInternalResponseV1(
                result.productId(),
                result.name(),
                result.firmId(),
                result.stockQuantity(),
                result.price()
        );
    }
}
