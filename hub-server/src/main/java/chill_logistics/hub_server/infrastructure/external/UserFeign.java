package chill_logistics.hub_server.infrastructure.external;

import chill_logistics.hub_server.infrastructure.external.dto.response.UserResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-server")
public interface UserFeign {

    @GetMapping("v1/internal/users/{userId}")
    UserResponseV1 getUser(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable("userId") UUID userId);
}
