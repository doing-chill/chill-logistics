package chill_logistics.firm_server.application.port;

import chill_logistics.firm_server.infrastructure.external.dto.response.FeignUserResponseV1;
import java.util.UUID;
import lib.web.response.BaseResponse;

public interface UserClient {

    BaseResponse<FeignUserResponseV1> readUserInfo(UUID userId);

}
