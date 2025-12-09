package chill_logistics.delivery_server.infrastructure.kafka.dto;

import chill_logistics.delivery_server.application.dto.command.HubRouteAfterCommandV1;
import chill_logistics.delivery_server.application.dto.command.HubRouteHubInfoV1;
import java.math.BigDecimal;
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
    int expectedDeliveryDuration,
    List<HubRouteHubInfoV1> pathHubs,
    BigDecimal totalDistanceKm) {

    // application 계층의 command 변환 메서드
    public HubRouteAfterCommandV1 toCommand() {
        return new HubRouteAfterCommandV1(
            orderId(),
            receiverFirmId(),
            receiverFirmFullAddress(),
            receiverFirmOwnerName(),
            requestNote(),
            productName(),
            productQuantity(),
            orderCreatedAt(),
            expectedDeliveryDuration(),
            pathHubs()
        );
    }
}
