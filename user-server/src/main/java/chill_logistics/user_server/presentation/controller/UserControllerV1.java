package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.service.UserQueryServiceV1;
import chill_logistics.user_server.presentation.dto.response.UserInfoResponseDtoV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "4.유저 정보")
public class UserControllerV1 {

    private final UserQueryServiceV1 userQueryServiceV1;

    @GetMapping("/info")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자가 자신의 정보를 조회하는 API입니다.")
    public BaseResponse<UserInfoResponseDtoV1> readUserInfo() {

        UserInfoResponseDtoV1 response = UserInfoResponseDtoV1.from(userQueryServiceV1.readUserInfo());

        return BaseResponse.ok(response, BaseStatus.OK);
    }
}
