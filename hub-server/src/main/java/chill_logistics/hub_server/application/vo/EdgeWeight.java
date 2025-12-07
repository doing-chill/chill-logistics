package chill_logistics.hub_server.application.vo;

import java.math.BigDecimal;
import java.util.UUID;

public record EdgeWeight(
    UUID fromHubId,
    UUID toHubId,
    int durationSec,
    BigDecimal distanceKm
) {}