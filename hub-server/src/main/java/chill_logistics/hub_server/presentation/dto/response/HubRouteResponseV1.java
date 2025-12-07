package chill_logistics.hub_server.presentation.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record HubRouteResponseV1 (

    UUID startHubId,
    UUID endHubId,
    List<UUID> pathHubIds,
    int totalDurationSec,
    long totalHours,
    long totalMinutes,
    BigDecimal totalDistanceKm
    ){}
