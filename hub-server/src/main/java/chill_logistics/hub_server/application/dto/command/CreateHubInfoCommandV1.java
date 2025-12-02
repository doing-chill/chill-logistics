package chill_logistics.hub_server.application.dto.command;


import java.util.UUID;

public record CreateHubInfoCommandV1 (
    UUID startHubId,
    UUID endHubId
){}
