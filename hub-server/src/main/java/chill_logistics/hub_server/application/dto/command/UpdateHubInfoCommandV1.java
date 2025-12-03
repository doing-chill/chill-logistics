package chill_logistics.hub_server.application.dto.command;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateHubInfoCommandV1 (
    UUID startHubId,
    UUID endHubId)
{ }
