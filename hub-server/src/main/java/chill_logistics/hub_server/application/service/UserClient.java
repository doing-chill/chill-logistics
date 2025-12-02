package chill_logistics.hub_server.application.service;

import chill_logistics.hub_server.infrastructure.external.dto.response.UserResponseV1;
import java.util.UUID;

public interface UserClient {

    UserResponseV1 getUser(UUID userId);

}
