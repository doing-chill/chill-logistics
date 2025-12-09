package chill_logistics.firm_server.application.port;

import chill_logistics.firm_server.infrastructure.external.dto.response.FeignHubResponseV1;
import java.util.UUID;

public interface HubClient {

    boolean readHubInfo(UUID hubId);

}
