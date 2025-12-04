package chill_logistics.order_server.application.dto.command;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderCommandV1(
        UUID supplierFirmId,
        UUID receiverFirmId,
        String requestNote,
        List<CreateOrderProductCommandV1> productList
) {}
