package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.query.ReadOrderDetailResultV1;
import chill_logistics.order_server.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ReadOrderDetailResponseV1(
        UUID id,
        SupplierResponseV1 supplier,
        ReceiverResponseV1 receiver,
        List<OrderProductResponseV1> orderProductList,
        OrderStatus orderStatus,
        String requestNote,
        LocalDateTime createdAt,
        UUID createdBy,
        LocalDateTime updatedAt,
        UUID updatedBy,
        LocalDateTime deletedAt,
        UUID deletedBy
) {
    public static ReadOrderDetailResponseV1 from(ReadOrderDetailResultV1 result) {
        return new ReadOrderDetailResponseV1(
                result.id(),
                SupplierResponseV1.from(result.supplier()),
                ReceiverResponseV1.from(result.receiver()),
                result.orderProductList()
                        .stream()
                        .map(OrderProductResponseV1::from)
                        .toList(),
                result.orderStatus(),
                result.requestNote(),
                result.createdAt(),
                result.createdBy(),
                result.updatedAt(),
                result.updatedBy(),
                result.deletedAt(),
                result.deletedBy()
        );
    }
}
