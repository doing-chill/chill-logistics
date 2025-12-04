package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.query.OrderProductResultV1;

import java.util.UUID;

public record OrderProductResponseV1(
        UUID productId,
        String name,
        int quantity,
        int productPrice
) {
    public static OrderProductResponseV1 from(OrderProductResultV1 result) {
        return new OrderProductResponseV1(
                result.productId(),
                result.name(),
                result.quantity(),
                result.productPrice()
        );
    }
}
