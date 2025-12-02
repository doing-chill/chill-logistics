package chill_logistics.product_server.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateProductRequestV1(

        @NotBlank(message = "상품명은 필수입니다.")
        String name,

        @NotNull(message = "firmId는 필수입니다.")
        UUID firmId,

        @NotNull(message = "hubId는 필수입니다.")
        UUID hubId,

        @PositiveOrZero(message = "재고 수량은 0개 이상이어야 합니다.")
        Integer stockQuantity,

        @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
        Integer price,

        Boolean sellable
) { }
