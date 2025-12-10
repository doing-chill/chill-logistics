package chill_logistics.firm_server.application.dto.query;

import java.util.UUID;

public record FirmSearchInfoQueryV1(
    UUID id,
    String name,
    UUID hubId,
    String firmFullAddress,
    String firmOwnerName
){
    public static FirmSearchInfoQueryV1 from (    UUID id,
        String name,
        UUID hubId,
        String firmFullAddress,
        String firmOwnerName) {
        return new FirmSearchInfoQueryV1(id, name, hubId, firmFullAddress, firmOwnerName);
    }
}
