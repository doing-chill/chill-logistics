package chill_logistics.product_server.presentation.dto.request;

import java.util.UUID;

public record CreateProductRequestV1(
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) { }
