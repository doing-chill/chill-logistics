package chill_logistics.order_server.presentation.dto.response;

import chill_logistics.order_server.application.dto.query.SupplierResultV1;

import java.util.UUID;

public record SupplierResponseV1(
        UUID id,
        String name,
        String address
) {
    public static SupplierResponseV1 from(SupplierResultV1 result) {
        return new SupplierResponseV1(
                result.id(),
                result.name(),
                result.address()
        );
    }
}
