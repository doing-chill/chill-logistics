package chill_logistics.order_server.application.dto.query;

import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReadOrderDetailResultV1(
        UUID id,
        SupplierResultV1 supplier,
        ReceiverResultV1 receiver,
        List<OrderProductResultV1> orderProductList,
        OrderStatus orderStatus,
        String requestNote,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy,
        LocalDateTime deletedAt,
        UUID deletedBy
) {
    public static ReadOrderDetailResultV1 from(
            Order order,
            FirmQueryResultV1 supplierResult,
            FirmQueryResultV1 receiverResult) {
        return new ReadOrderDetailResultV1(
                order.getId(),
                SupplierResultV1.from(supplierResult),
                ReceiverResultV1.from(receiverResult),
                order.getOrderProductList()
                        .stream()
                        .map(OrderProductResultV1::from)
                        .toList(),
                order.getOrderStatus(),
                order.getRequestNote(),
                order.getCreatedAt(),
                order.getCreatedBy(),
                order.getUpdatedAt(),
                order.getUpdatedBy(),
                order.getDeletedAt(),
                order.getDeletedBy()
        );
    }
}
