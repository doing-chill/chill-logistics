package chill_logistics.hub_server.infrastructure.external.dto.request;

import chill_logistics.hub_server.presentation.dto.response.HubsRouteInfoResponseV1;
import java.util.List;
import java.util.UUID;

public record HubRouteHubInfoV1 (
    UUID hubId,
    String hubName,
    String hubFullAddress
){
    public static List<HubRouteHubInfoV1> from(List<HubsRouteInfoResponseV1> response) {
        return response.stream().map(r->
            new HubRouteHubInfoV1(r.hubId(), r.hubName(), r.hubFullAddress())).toList();
    }
}
