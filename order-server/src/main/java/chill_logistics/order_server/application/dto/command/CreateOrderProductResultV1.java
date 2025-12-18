package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.application.dto.query.OrderProductResultV1;
import chill_logistics.order_server.domain.entity.OrderProduct;

import java.util.UUID;

public record CreateOrderProductResultV1(
        UUID id,
        String name,
        int quantity,
        int price
) {
    public static CreateOrderProductResultV1 from(OrderProduct orderProduct) {
        return new CreateOrderProductResultV1(
                orderProduct.getProductId(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getProductPrice()
        );
    }

    public static CreateOrderProductResultV1 fromReadOrder(OrderProductResultV1 result) {
        return new CreateOrderProductResultV1(
                result.productId(),
                result.name(),
                result.quantity(),
                result.productPrice()
        );
    }
}
