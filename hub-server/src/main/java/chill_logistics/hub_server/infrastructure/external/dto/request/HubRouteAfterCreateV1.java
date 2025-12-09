package chill_logistics.hub_server.infrastructure.external.dto.request;

import chill_logistics.hub_server.application.dto.response.HubRouteResultResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HubRouteAfterCreateV1(
    UUID orderId,
    UUID receiverFirmId,
    String receiverFirmFullAddress,
    String receiverFirmOwnerName,
    String requestNote,
    String productName,
    int productQuantity,
    LocalDateTime orderCreatedAt,
    Integer expectedDeliveryDuration,
    List<HubRouteHubInfoV1> hubsRouteInfoResponse
) {
    public static HubRouteAfterCreateV1 from(HubRouteResultResponse response, List<HubRouteHubInfoV1> hubsRouteInfoResponse) {
        return new HubRouteAfterCreateV1(response.orderId(), response.receiverFirmId(), response.receiverFirmFullAddress(),
            response.receiverFirmOwnerName(), response.requestNote(), response.productName(), response.productQuantity(), response.orderCreatedAt(),
            response.expectedDeliveryDuration(), hubsRouteInfoResponse);
    }
}