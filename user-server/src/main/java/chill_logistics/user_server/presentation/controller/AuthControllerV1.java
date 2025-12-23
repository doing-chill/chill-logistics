package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.dto.command.*;
import chill_logistics.user_server.application.service.AuthServiceV1;
import chill_logistics.user_server.presentation.dto.request.LoginRequestDtoV1;
import chill_logistics.user_server.presentation.dto.response.LoginResponseDtoV1;
import chill_logistics.user_server.presentation.dto.response.ReissueTokenResponseDtoV1;
import chill_logistics.user_server.presentation.dto.request.SignupRequestDtoV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@Tag(name = "1.회원 관리", description = "로그인, 회원가입 관리용 API")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthServiceV1 authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 API입니다.")
    public BaseResponse<Void> signup(@RequestBody SignupRequestDtoV1 dto) {

        authService.signup(
                new SignupCommandV1(
                        dto.email(),
                        dto.username(),
                        dto.nickname(),
                        dto.password()
                )
        );

        return BaseResponse.ok(BaseStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API입니다.")
    public BaseResponse<LoginResponseDtoV1> login(@RequestBody LoginRequestDtoV1 dto) {

        LoginResultV1 result = authService.login(
                new LoginCommandV1(dto.email(), dto.password())
        );

        return BaseResponse.ok(
                new LoginResponseDtoV1(result.accessToken(), result.refreshToken()),
                BaseStatus.OK
        );
    }

    @PostMapping("/reissue-token")
    @Operation(summary = "리프레시 토큰 발급", description = "리프레시 토큰 발급 API입니다.")
    public BaseResponse<ReissueTokenResponseDtoV1> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        if (authHeader == null || authHeader.isBlank()) {
            throw new IllegalArgumentException("Authorization 헤더가 없습니다.");
        }

        String token = authHeader.trim();
        if (token.regionMatches(true, 0, "Bearer", 0, 6)) {
            token = token.substring(6).trim();
        }

        ReissueTokenResultV1 result = authService.reissueToken(
                new ReissueTokenCommandV1(token)
        );

        return BaseResponse.ok(
                new ReissueTokenResponseDtoV1(
                        result.accessToken(),
                        result.refreshToken()),
                BaseStatus.OK
        );
    }
}
