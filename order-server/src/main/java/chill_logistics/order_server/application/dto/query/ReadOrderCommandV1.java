package chill_logistics.order_server.application.dto.query;

import chill_logistics.order_server.domain.entity.OrderStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadOrderCommandV1(
        UUID supplierFirmId,
        UUID receiverFirmId,
        OrderStatus orderStatus
) {}
