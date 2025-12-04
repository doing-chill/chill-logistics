package chill_logistics.delivery_server.application.dto.query;

import chill_logistics.delivery_server.domain.entity.HubDelivery;
import java.util.UUID;

public record HubDeliverySummaryResponseV1(
    UUID hubDeliveryId,
    UUID orderId,
    String startHubName) {

    public static HubDeliverySummaryResponseV1 from(HubDelivery hubDelivery) {

        return new HubDeliverySummaryResponseV1(
            hubDelivery.getId(),
            hubDelivery.getOrderId(),
            hubDelivery.getStartHubName()
        );
    }
}
