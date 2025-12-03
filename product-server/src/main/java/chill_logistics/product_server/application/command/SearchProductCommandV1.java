package chill_logistics.product_server.application.command;

import java.util.UUID;

public record SearchProductCommandV1(
        String name,
        UUID firmId,
        UUID hubId,
        Boolean sellable
) {}
