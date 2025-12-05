package chill_logistics.order_server.presentation.dto.request;

import chill_logistics.order_server.application.dto.query.ReadOrderCommandV1;
import chill_logistics.order_server.domain.entity.OrderStatus;

import java.util.UUID;

public record ReadOrderRequestV1(
        UUID supplierFirmId,
        UUID receiverFirmId,
        OrderStatus orderStatus
) {
    public ReadOrderCommandV1 toCommand() {
        return ReadOrderCommandV1.builder()
                .supplierFirmId(this.supplierFirmId)
                .receiverFirmId(this.receiverFirmId)
                .orderStatus(this.orderStatus)
                .build();
    }
}
