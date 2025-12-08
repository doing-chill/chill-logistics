package chill_logistics.product_server.application.dto.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateStockRecoverCommandV1(
        UUID id,
        int quantity
) {}
