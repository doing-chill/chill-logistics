package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record SupplierInfoV1(
        UUID id,
        UUID hubId
) {
    public static SupplierInfoV1 from(FirmResultV1 result) {
        return new SupplierInfoV1(
                result.id(),
                result.hubId()
        );
    }
}
