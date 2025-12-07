package chill_logistics.hub_server.application.route;

import java.util.UUID;
import lombok.Getter;

@Getter
public class HubRouteNode {

    private final UUID hubId;
    private final int time;

    public HubRouteNode(UUID hubId, int time) {
        this.hubId = hubId;
        this.time = time;
        }

}
