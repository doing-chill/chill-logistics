package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.command.MasterUpdateUserInfoCommandV1;
import chill_logistics.user_server.application.service.MasterServiceV1;
import chill_logistics.user_server.presentation.dto.MasterUpdateUserInfoRequestDtoV1;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/master/users")
@Slf4j
public class MasterControllerV1 {

    private final MasterServiceV1 masterService;

    @PutMapping("/{userId}/info")
    @PreAuthorize("hasRole('MASTER')")
    public BaseResponse<Void> updateUserInfo(
            @PathVariable UUID userId,
            @RequestBody MasterUpdateUserInfoRequestDtoV1 request) {

        log.info("[MasterControllerV1] ===== 컨트롤러 진입 - userId={} =====", userId);

        MasterUpdateUserInfoCommandV1 command = request.toCommand(userId);
        masterService.updateUserInfo(command);

        log.info("[MasterControllerV1] ===== 컨트롤러 종료 - userId={} =====", userId);

        return BaseResponse.ok(BaseStatus.OK);
    }
}
