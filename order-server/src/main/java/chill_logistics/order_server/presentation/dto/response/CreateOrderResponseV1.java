package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.command.CreateOrderResultV1;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateOrderResponseV1(
        UUID id,
        UUID supplierId,
        String supplierName,
        UUID receiverId,
        String receiverName,
        String requestNote,
        List<CreateOrderProductResponseV1> productList,
        LocalDateTime createdAt
) {
    public static CreateOrderResponseV1 from(CreateOrderResultV1 result) {
        return new CreateOrderResponseV1(
                result.id(),
                result.supplierId(),
                result.supplierName(),
                result.receiverId(),
                result.receiverName(),
                result.requestNote(),
                result.productList()
                        .stream()
                        .map(CreateOrderProductResponseV1::from)
                        .toList(),
                result.createdAt()
        );
    }
}
