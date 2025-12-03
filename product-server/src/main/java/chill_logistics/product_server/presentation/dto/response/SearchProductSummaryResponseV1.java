package chill_logistics.product_server.presentation.dto.response;

import chill_logistics.product_server.application.dto.query.SearchProductSummaryResultV1;

import java.util.UUID;

public record SearchProductSummaryResponseV1(
        UUID id,
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) {
    public static SearchProductSummaryResponseV1 from(SearchProductSummaryResultV1 result) {
        return new SearchProductSummaryResponseV1(
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
