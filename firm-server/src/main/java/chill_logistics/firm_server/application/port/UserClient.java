package chill_logistics.firm_server.application.port;

import chill_logistics.firm_server.infrastructure.external.dto.response.FeignUserResponseV1;
import java.util.UUID;

public interface UserClient {

    FeignUserResponseV1 readUserInfo(UUID userId);

}
