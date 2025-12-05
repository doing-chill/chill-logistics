package chill_logistics.order_server.presentation.dto.request;

import chill_logistics.order_server.application.dto.command.CreateOrderCommandV1;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestV1(
        UUID supplierFirmId,
        UUID receiverFirmId,
        String requestNote,
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
