package chill_logistics.order_server.application.dto.query;

import java.util.UUID;

public record SupplierResultV1(
        UUID id,
        String name,
        String address
) {
    public static SupplierResultV1 from(FirmQueryResultV1 result) {
        return new SupplierResultV1(
                result.id(),
                result.name(),
                result.address()
        );
    }
}
