package chill_logistics.order_server.infrastructure.hub.dto;

import java.util.UUID;

public record UserHubsResponseV1(
        UUID managingHubId
) {}
