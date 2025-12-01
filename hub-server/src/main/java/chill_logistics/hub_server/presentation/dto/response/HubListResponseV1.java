package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record HubListResponseV1(
   UUID hubId,
   String name,
   String fullAddress
){
    public static List<HubListResponseV1>  fromHubListQuery(List<HubListQueryV1> hubListQueryV1) {
        List<HubListResponseV1> hubListResponse = new ArrayList<>();
        for (HubListQueryV1 hubQueryItem : hubListQueryV1) {
            hubListResponse.add(new HubListResponseV1(hubQueryItem.hubId(), hubQueryItem.name(), hubQueryItem.fullAddress()));
        }
        return hubListResponse;
    }
}
