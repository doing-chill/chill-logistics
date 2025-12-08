package chill_logistics.order_server.presentation.dto.request;

import chill_logistics.order_server.application.dto.command.CreateOrderCommandV1;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestV1(

        @NotNull(message = "공급 업체 id는 필수입니다.")
        UUID supplierFirmId,

        @NotNull(message = "수령 업체 id는 필수입니다.")
        UUID receiverFirmId,

        String requestNote,

        @NotNull(message = "주문 상품 목록은 필수입니다.")
        @Size(min = 1, message = "주문 상품 목록은 최소 1개 이상의 상품을 포함해야 합니다.")
        List<CreateOrderProductRequestV1> productList
) {
    public CreateOrderCommandV1 toCommand() {
        return CreateOrderCommandV1.builder()
                .supplierFirmId(this.supplierFirmId)
                .receiverFirmId(this.receiverFirmId)
                .requestNote(this.requestNote)
                .productList(
                        this.productList.stream()
                                .map(CreateOrderProductRequestV1::toCommand)
                                .toList()
                )
                .build();
    }
}
