package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.HubListQuery;
import java.util.UUID;

public record HubListResponseV1(
   UUID hubId,
   String name,
   String fullAddress
){
    public static HubListResponseV1 fromHubListQuery(HubListQuery hubListQuery) {
        return new HubListResponseV1(hubListQuery.hubId(), hubListQuery.name(), hubListQuery.fullAddress());
    }
}
