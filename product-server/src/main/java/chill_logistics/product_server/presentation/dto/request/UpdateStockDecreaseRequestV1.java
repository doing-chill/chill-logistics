package chill_logistics.product_server.presentation.dto.request;

import chill_logistics.product_server.application.dto.command.UpdateStockDecreaseCommandV1;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateStockDecreaseRequestV1(
        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        int quantity
) {
    public UpdateStockDecreaseCommandV1 toCommand(UUID id) {
        return UpdateStockDecreaseCommandV1.builder()
                .id(id)
                .quantity(this.quantity)
                .build();
    }
}
