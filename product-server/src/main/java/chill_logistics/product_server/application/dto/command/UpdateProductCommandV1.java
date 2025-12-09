package chill_logistics.product_server.application.dto.command;

import lombok.Builder;

@Builder
public record UpdateProductCommandV1(
        String name,
        Integer stockQuantity,
        Integer price,
        Boolean sellable
) {}
