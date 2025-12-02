package chill_logistics.hub_server.infrastructure.external;

import chill_logistics.hub_server.infrastructure.external.dto.response.UserResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-server")
public interface UserFeign {

    @GetMapping("v1/users/{userId}")
    UserResponseV1 getUser(@PathVariable("userId") UUID userId);


}
