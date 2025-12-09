package chill_logistics.hub_server.presentation.dto.response;

import chill_logistics.hub_server.application.dto.query.UserHubsQueryV1;
import java.util.List;
import java.util.UUID;

public record UserHubsResponseV1 (
    UUID managingHubId
){
    public static List<UserHubsResponseV1> from (List<UserHubsQueryV1> querys){
        return querys.stream()
            .map(query -> new UserHubsResponseV1(query.managingHubId()))
            .toList();
    }
}
