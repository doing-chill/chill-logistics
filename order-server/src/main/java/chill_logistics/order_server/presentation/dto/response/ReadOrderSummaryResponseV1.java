package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.query.ReadOrderSummaryResultV1;
import chill_logistics.order_server.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadOrderSummaryResponseV1(
        UUID id,
        UUID supplierFirmId,
        UUID receiverFirmId,
        String requestNote,
        OrderStatus orderStatus,
        LocalDateTime createdAt
) {
    public static ReadOrderSummaryResponseV1 from(ReadOrderSummaryResultV1 result) {
        return new ReadOrderSummaryResponseV1(
                result.id(),
                result.supplierFirmId(),
                result.receiverFirmId(),
                result.requestNote(),
                result.orderStatus(),
                result.createdAt()
        );
    }
}
