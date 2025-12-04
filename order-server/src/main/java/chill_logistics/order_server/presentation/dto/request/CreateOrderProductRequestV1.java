package chill_logistics.order_server.presentation.dto.request;

import chill_logistics.order_server.application.dto.command.CreateOrderProductCommandV1;

import java.util.UUID;

public record CreateOrderProductRequestV1(
        UUID productId,
        Integer quantity
) {
    public CreateOrderProductCommandV1 toCommand() {
        return CreateOrderProductCommandV1.builder()
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }
}
