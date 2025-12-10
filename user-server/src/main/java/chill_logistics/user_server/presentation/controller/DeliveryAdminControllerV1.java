package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.dto.command.AssignDeliveryAdminCommandV1;
import chill_logistics.user_server.application.service.DeliveryAdminCommandServiceV1;
import chill_logistics.user_server.presentation.dto.request.AssignDeliveryAdminRequestDtoV1;
import chill_logistics.user_server.presentation.dto.response.AssignDeliveryAdminResponseDtoV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name = "3.배송 담당자", description = "배송담당자 관리용 API")
@RequiredArgsConstructor
public class DeliveryAdminControllerV1 {

    private final DeliveryAdminCommandServiceV1 deliveryAdminCommandServiceV1;

    @PutMapping("/internal/delivery-admin/assign")
    @Operation(summary = "배송 담당자 배정", description = "허브 기반으로 배송 담당자를 배정할 때 사용하는 API입니다.")
    public BaseResponse<AssignDeliveryAdminResponseDtoV1> assignDeliveryAdmin(
            @RequestBody AssignDeliveryAdminRequestDtoV1 dto) {

        AssignDeliveryAdminCommandV1 command = dto.toCommand();
        AssignDeliveryAdminResponseDtoV1 response = deliveryAdminCommandServiceV1.assignDeliveryAdmin(command);

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
