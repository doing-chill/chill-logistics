package chill_logistics.product_server.application.dto.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateProductCommandV1(
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) {}
