package chill_logistics.product_server.application.command;

import java.util.UUID;

public record CreateProductCommandV1(
        String name,
        UUID firmId,
        UUID hubId,
        int stockQuantity,
        int price,
        boolean sellable
) { }
