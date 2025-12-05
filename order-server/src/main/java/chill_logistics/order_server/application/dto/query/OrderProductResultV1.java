package chill_logistics.order_server.application.dto.query;

import chill_logistics.order_server.domain.entity.OrderProduct;

import java.util.UUID;

public record OrderProductResultV1(
        UUID productId,
        String name,
        int quantity,
        int productPrice
) {
    public static OrderProductResultV1 from(OrderProduct orderProduct) {
        return new OrderProductResultV1(
                orderProduct.getProductId(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getProductPrice()
        );
    }
}
