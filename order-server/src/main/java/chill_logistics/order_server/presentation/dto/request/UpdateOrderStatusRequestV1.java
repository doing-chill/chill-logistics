package chill_logistics.order_server.presentation.dto.request;

import chill_logistics.order_server.application.dto.command.UpdateOrderStatusCommandV1;
import chill_logistics.order_server.domain.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestV1(
        @NotNull
        OrderStatus status
) {
    public UpdateOrderStatusCommandV1 toCommand() {
        return UpdateOrderStatusCommandV1.builder()
                .status(this.status)
                .build();
    }
}
