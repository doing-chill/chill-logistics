package chill_logistics.hub_server.infrastructure.external;

import chill_logistics.hub_server.application.service.UserClient;
import chill_logistics.hub_server.infrastructure.external.dto.response.UserResponseV1;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserClientImpl implements UserClient {

    private final UserFeign userFeign;


    @Override
    public UserResponseV1 getUser(UUID userId) {
        return userFeign.getUser(userId);
    }
}
