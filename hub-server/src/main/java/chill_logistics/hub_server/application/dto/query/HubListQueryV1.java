package chill_logistics.hub_server.application.dto.query;

import chill_logistics.hub_server.domain.entity.Hub;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record HubListQueryV1(

    UUID hubId,
    String name,
    String fullAddress

){
    public static List<HubListQueryV1> fromHubs(List<Hub> hubs) {
        List<HubListQueryV1> hubListQueryV1 = new ArrayList<>();
        for (Hub hub : hubs) {
            hubListQueryV1.add(new HubListQueryV1(hub.getId(), hub.getName(), hub.getFullAddress()));
        }
        return hubListQueryV1;
    }

}
