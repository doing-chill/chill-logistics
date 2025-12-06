package chill_logistics.order_server.application.dto.query;

import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadOrderSummaryResultV1(
        UUID id,
        UUID supplierFirmId,
        UUID receiverFirmId,
        String requestNote,
        OrderStatus orderStatus,
        LocalDateTime createdAt
) {
    public static ReadOrderSummaryResultV1 from(Order order) {
        return new ReadOrderSummaryResultV1(
                order.getId(),
                order.getSupplierFirmId(),
                order.getReceiverFirmId(),
                order.getRequestNote(),
                order.getOrderStatus(),
                order.getCreatedAt()
        );
    }
}
