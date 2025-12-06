package chill_logistics.product_server.application.dto.query;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateStockDecreaseCommandV1(
        UUID id,
        int quantity
) {}
