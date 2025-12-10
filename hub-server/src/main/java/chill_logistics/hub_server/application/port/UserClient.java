package chill_logistics.hub_server.application.port;

import chill_logistics.hub_server.infrastructure.external.dto.response.FeignUserResponseV1;
import java.util.UUID;
import lib.web.response.BaseResponse;

public interface UserClient {

    BaseResponse<FeignUserResponseV1> readUserInfo(UUID userId);

}
