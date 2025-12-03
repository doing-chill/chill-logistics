package chill_logistics.product_server.presentation.dto.response;

import chill_logistics.product_server.application.dto.query.ReadProductSummaryResultV1;

import java.util.UUID;

public record ReadProductSummaryResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) {
    public static ReadProductSummaryResponseV1 from(ReadProductSummaryResultV1 result) {
        return new ReadProductSummaryResponseV1(
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
