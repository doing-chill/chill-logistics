package chill_logistics.product_server.presentation.dto.request;

import java.util.UUID;

public record SearchProductRequestV1(
        String name,
        UUID firmId,
        UUID hubId,
        Boolean sellable
) {}
