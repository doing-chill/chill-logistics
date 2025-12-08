package chill_logistics.hub_server.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HubRouteResultResponse (
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
    int expectedDeliveryDuration,
    List<UUID>pathHubIds,
    BigDecimal totalDistanceKm
){}