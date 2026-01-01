package chill_logistics.hub_server.application.dto.query;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record HubListInfoQueryV1(
    UUID hubId,
    String name,
    String fullAddress
){
    public static HubListInfoQueryV1 from(Hub hub) {

        return new HubListInfoQueryV1(hub.getId(), hub.getName(), hub.getFullAddress());
    }

}
