package chill_logistics.hub_server.infrastructure.external.dto.response;

import chill_logistics.hub_server.application.dto.response.HubRouteResultResponse;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubRouteAfterCreateV1(
    UUID orderId,
    UUID startHubId,
    String startHubName,
    String startHubFullAddress,
    UUID endHubId,
    String endHubName,
    String endHubFullAddress,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt,
    Integer expectedDeliveryDuration
) {
    public static HubRouteAfterCreateV1 from(HubRouteResultResponse response) {
        return new HubRouteAfterCreateV1(response.orderId(), response.startHubId(), response.startHubName(), response.startHubFullAddress(),
            response.endHubId(), response.endHubName(), response.endHubFullAddress(), response.receiverFirmId(), response.receiverFirmFullAddress(),
            response.receiverFirmOwnerName(), response.requestNote(), response.productName(), response.productQuantity(), response.orderCreatedAt(),
            response.expectedDeliveryDuration());
    }

}