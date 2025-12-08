package chill_logistics.order_server.presentation.dto.request;

import chill_logistics.order_server.application.dto.command.CreateOrderProductCommandV1;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderProductRequestV1(

        @NotNull(message = "상품id는 필수입니다.")
        UUID productId,

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int quantity
) {
    public CreateOrderProductCommandV1 toCommand() {
        return CreateOrderProductCommandV1.builder()
                .productId(this.productId)
                .quantity(this.quantity)
                .build();
    }
}
