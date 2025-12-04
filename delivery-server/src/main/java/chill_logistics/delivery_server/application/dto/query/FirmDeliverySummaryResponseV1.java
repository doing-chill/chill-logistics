package chill_logistics.delivery_server.application.dto.query;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import java.util.UUID;

public record FirmDeliverySummaryResponseV1(
    UUID firmDeliveryId,
    UUID orderId,
    String receiverFirmOwnerName) {
    public static FirmDeliverySummaryResponseV1 from(FirmDelivery firmDelivery) {

        return new FirmDeliverySummaryResponseV1(
            firmDelivery.getId(),
            firmDelivery.getOrderId(),
            firmDelivery.getReceiverFirmOwnerName()
        );
    }
}
