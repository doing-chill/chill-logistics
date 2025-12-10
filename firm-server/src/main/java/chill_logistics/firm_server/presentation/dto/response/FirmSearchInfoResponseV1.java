package chill_logistics.firm_server.presentation.dto.response;

import chill_logistics.firm_server.application.dto.query.FirmSearchInfoQueryV1;
import java.util.UUID;

public record FirmSearchInfoResponseV1 (
    UUID id,
    String name,
    UUID hubId,
    String firmFullAddress,
    String firmOwnerName
){
    public static FirmSearchInfoResponseV1 from(FirmSearchInfoQueryV1 firmSearchInfoQuery) {
        return new FirmSearchInfoResponseV1(firmSearchInfoQuery.id(),
            firmSearchInfoQuery.firmOwnerName(), firmSearchInfoQuery.hubId(),
            firmSearchInfoQuery.firmFullAddress(), firmSearchInfoQuery.firmOwnerName());
    }
}
