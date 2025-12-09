package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.vo.HubRouteStep;
import java.util.List;
import java.util.UUID;

public record HubsRouteInfoResponseV1 (

    UUID hubId,
    String hubName,
    String hubFullAddress

){
    public static List<HubsRouteInfoResponseV1> from (List<HubRouteStep> hubRouteSteps) {
        return hubRouteSteps.stream().map(step ->
            new HubsRouteInfoResponseV1(step.hubId(), step.hubName(), step.hubFullAddress()))
            .toList();
    }
}