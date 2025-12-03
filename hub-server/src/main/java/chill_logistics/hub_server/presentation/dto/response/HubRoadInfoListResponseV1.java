package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.HubRoadInfoListQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record HubRoadInfoListResponseV1 (

    UUID hubInfoId,
    UUID startHubId,
    String startHubName,
    UUID endHubId,
    String endHubName )
{
    public static List<HubRoadInfoListResponseV1> from (List<HubRoadInfoListQuery> queries){
        List<HubRoadInfoListResponseV1 > hubRoadInfoListResponse = new ArrayList<>();

        for (HubRoadInfoListQuery query : queries) {
            hubRoadInfoListResponse.add(new HubRoadInfoListResponseV1 (query.hubInfoId(),
                query.startHubId(), query.startHubName(), query.endHubId(), query.endHubName()));
        }

        return hubRoadInfoListResponse;
    }
}
