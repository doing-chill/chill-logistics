package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.HubListInfoQueryV1;
import chill_logistics.hub_server.application.dto.query.HubListQueryV1;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record HubListResponseV1(
    List<HubListInfoQueryV1> contents,
    int page,
    int size,
    long totalCount,
    boolean hasNext,
    int totalPages
){
    public static HubListResponseV1 fromHubListQuery(HubListQueryV1 hubListQuery) {
        return new HubListResponseV1(hubListQuery.contents(), hubListQuery.page(),
            hubListQuery.size(), hubListQuery.totalCount(), hubListQuery.hasNext(),
            hubListQuery.totalPages());
    }
}
