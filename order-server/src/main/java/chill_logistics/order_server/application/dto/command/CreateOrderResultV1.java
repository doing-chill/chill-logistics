package chill_logistics.order_server.application.dto.command;

import chill_logistics.order_server.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResultV1(
        UUID id,
        UUID supplierId,
        String supplierName,
        UUID receiverId,
        String receiverName,
        String requestNote,
        List<CreateOrderProductResultV1> productList,
        LocalDateTime createdAt
) {
    public static CreateOrderResultV1 from(
            Order order,
            FirmInfoV1 supplier,
            FirmInfoV1 receiver) {
        return new CreateOrderResultV1(
                order.getId(),
                order.getSupplierFirmId(),
                supplier.name(),
                order.getReceiverFirmId(),
                receiver.name(),
                order.getRequestNote(),
                order.getOrderProductList()
                        .stream()
                        .map(CreateOrderProductResultV1::from)
                        .toList(),
                order.getCreatedAt()
        );
    }
}
