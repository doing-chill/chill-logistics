package chill_logistics.hub_server.application.dto.query;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.List;
import java.util.UUID;

public record UserHubsQueryV1(
   UUID managingHubId
){
    public static List<UserHubsQueryV1> from(List<Hub> hubs) {
        return hubs.stream()
            .map(hub -> new UserHubsQueryV1(hub.getId()))
            .toList();
    }
}
