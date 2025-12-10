package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.dto.command.UpdateUserInfoCommandV1;
import chill_logistics.user_server.application.service.UserCommandServiceV1;
import chill_logistics.user_server.application.service.UserQueryServiceV1;
import chill_logistics.user_server.presentation.dto.request.UpdateUserInfoRequestDtoV1;
import chill_logistics.user_server.presentation.dto.response.UserInfoResponseDtoV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "4.유저 정보")
public class UserControllerV1 {

    private final UserQueryServiceV1 userQueryService;
    private final UserCommandServiceV1 userCommandService;

    @GetMapping("/info")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자가 자신의 정보를 조회하는 API입니다.")
    public BaseResponse<UserInfoResponseDtoV1> readUserInfo() {

        UserInfoResponseDtoV1 response = UserInfoResponseDtoV1.from(userQueryService.readUserInfo());

        return BaseResponse.ok(response, BaseStatus.OK);
    }

    @PutMapping("/info")
    @Operation(summary = "내 정보 수정", description = "로그인한 사용자가 자신의 정보를 수정하는 API입니다.")
    public BaseResponse<Void> updateUserInfo(
            @RequestBody UpdateUserInfoRequestDtoV1 request
    ) {

        UpdateUserInfoCommandV1 command = request.toCommand();
        userCommandService.updateUserInfo(command);

        return BaseResponse.ok(BaseStatus.OK);

    }

}
