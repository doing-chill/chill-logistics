package chill_logistics.firm_server.infrastructure.external;

import chill_logistics.firm_server.application.port.UserClient;
import chill_logistics.firm_server.infrastructure.external.dto.response.FeignUserResponseV1;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignImpl implements UserClient {

    private final UserFeign userFeign;

    @Override
    public FeignUserResponseV1 readUserInfo(UUID userId) {
        return userFeign.readUserInfo(userId);
    }
}
