package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.command.CreateOrderProductResultV1;

import java.util.UUID;

public record CreateOrderProductResponseV1(
        UUID id,
        String name,
        int quantity,
        int price
) {
    public static CreateOrderProductResponseV1 from(CreateOrderProductResultV1 result) {
        return new CreateOrderProductResponseV1(
                result.id(),
                result.name(),
                result.quantity(),
                result.price()
        );
    }
}
