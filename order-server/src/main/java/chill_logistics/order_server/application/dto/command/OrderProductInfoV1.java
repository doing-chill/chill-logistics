package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record OrderProductInfoV1(
        UUID productId,
        String productName,
        int price,
        int quantity
) {}
