package chill_logistics.hub_server.application.vo;

import java.util.UUID;

public record HubRouteStep (
    UUID hubId,
    String hubName,
    String hubFullAddress)
{}

