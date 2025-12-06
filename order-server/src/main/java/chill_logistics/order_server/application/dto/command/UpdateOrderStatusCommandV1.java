package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.domain.entity.OrderStatus;
import lombok.Builder;

@Builder
public record UpdateOrderStatusCommandV1(
        OrderStatus status
) {}
