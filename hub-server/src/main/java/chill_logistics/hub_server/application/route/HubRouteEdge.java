package chill_logistics.hub_server.application.route;

import chill_logistics.hub_server.domain.entity.HubInfo;
import java.util.UUID;
import lombok.Getter;


@Getter
public class HubRouteEdge {

    private final UUID toHubId;
    private final HubInfo hubInfo;

    public HubRouteEdge(UUID toHubId, HubInfo hubInfo) {
        this.toHubId = toHubId;
        this.hubInfo = hubInfo;
    }
}
