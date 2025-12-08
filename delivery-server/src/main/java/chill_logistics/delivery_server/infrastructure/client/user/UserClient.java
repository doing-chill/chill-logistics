package chill_logistics.delivery_server.infrastructure.client.user;

import chill_logistics.delivery_server.infrastructure.client.user.dto.UserForDeliveryResponseV1;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "user-server",
    configuration = chill_logistics.delivery_server.infrastructure.config.FeignConfig.class
)
public interface UserClient {

    @GetMapping("/v1/users/{userId}")
    UserForDeliveryResponseV1 getUser(@PathVariable("userId") UUID userId);
}
