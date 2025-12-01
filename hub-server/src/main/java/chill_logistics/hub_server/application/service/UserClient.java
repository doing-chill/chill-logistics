package chill_logistics.hub_server.application.service;

import java.util.UUID;

public interface UserClient {

    UserResponseV1 getUser(UUID userId);

}
