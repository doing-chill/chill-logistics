package chill_logistics.firm_server.infrastructure.external;


import chill_logistics.firm_server.infrastructure.external.dto.response.FeignUserResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-server")

public interface UserFeign {

    @GetMapping("/v1/master/users/{userId}/info")
    FeignUserResponseV1 readUserInfo(@PathVariable UUID userId);

}
