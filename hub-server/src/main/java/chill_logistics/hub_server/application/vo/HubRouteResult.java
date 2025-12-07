package chill_logistics.hub_server.application.vo;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record HubRouteResult(
    UUID startHubId,
    UUID endHubId,
    List<UUID> pathHubIds,   // [허브1, 허브2, ..., 허브N]
    int totalDurationSec,
    BigDecimal totalDistanceKm
) {}