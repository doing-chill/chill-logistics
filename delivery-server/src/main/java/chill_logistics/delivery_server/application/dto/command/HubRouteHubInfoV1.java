package chill_logistics.delivery_server.application.dto.command;

import java.util.UUID;

public record HubRouteHubInfoV1(
    UUID hubId,
    String hubName,
    String hubFullAddress
) {}
