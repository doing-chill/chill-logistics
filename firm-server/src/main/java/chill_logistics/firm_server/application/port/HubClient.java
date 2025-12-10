package chill_logistics.firm_server.application.port;

import java.util.UUID;

public interface HubClient {

    boolean readHubInfo(UUID hubId);

}
