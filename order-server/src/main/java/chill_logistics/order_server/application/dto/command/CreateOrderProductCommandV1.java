package chill_logistics.order_server.application.dto.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateOrderProductCommandV1(
        UUID productId,
        Integer quantity
) {}
