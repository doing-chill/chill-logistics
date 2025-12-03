package chill_logistics.product_server.application.dto.query;

import java.util.UUID;

public record ReadProductCommandV1(
        String name,
        UUID firmId,
        UUID hubId,
        Boolean sellable
) {}
