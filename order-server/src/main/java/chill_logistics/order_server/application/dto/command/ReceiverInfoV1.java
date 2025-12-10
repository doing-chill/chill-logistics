package chill_logistics.order_server.application.dto.command;

import java.util.UUID;

public record ReceiverInfoV1(
        UUID id,
        UUID hubId,
        String firmFullAddress,
        String firmOwnerName
) {
    public static ReceiverInfoV1 from(FirmResultV1 result) {
        return new ReceiverInfoV1(
                result.id(),
                result.hubId(),
                result.firmFullAddress(),
                result.firmOwnerName()
        );
    }
}
