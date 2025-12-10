package chill_logistics.user_server.infrastructure.hub;

import chill_logistics.user_server.domain.port.HubPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubApiClientV1 implements HubPort {

    private final HubFeignClientV1 hubFeignClientV1;


    @Override
    public String readHubName(UUID hubId) {
        return hubFeignClientV1
                .getHubInfo(hubId)
                .getData()
                .name();
    }
}
