package chill_logistics.firm_server.infrastructure.external;

import chill_logistics.firm_server.application.port.HubClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubFeignImpl implements HubClient {

    private final HubFeign hubFeign;

    @Override
    public boolean readHubInfo(UUID hubId) {
        return hubFeign.readHubInfo(hubId);
    }
}
