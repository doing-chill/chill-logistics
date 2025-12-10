package chill_logistics.delivery_server.infrastructure.user;

import chill_logistics.delivery_server.infrastructure.user.dto.request.AssignDeliveryAdminRequestDtoV1;
import chill_logistics.delivery_server.infrastructure.user.dto.response.AssignDeliveryAdminResponseDtoV1;
import lib.web.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "user-server",
    configuration = chill_logistics.delivery_server.infrastructure.config.FeignConfig.class
)
public interface UserClient {

    @PutMapping("/v1/internal/delivery-admin/assign")
    BaseResponse<AssignDeliveryAdminResponseDtoV1> assignDeliveryAdmin(
        @RequestBody AssignDeliveryAdminRequestDtoV1 dto);
}
