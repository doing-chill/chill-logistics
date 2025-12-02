package chill_logistics.user_server.presentation.controller;

import chill_logistics.user_server.application.command.*;
import chill_logistics.user_server.application.service.AuthServiceV1;
import chill_logistics.user_server.presentation.dto.LoginRequestDtoV1;
import chill_logistics.user_server.presentation.dto.LoginResponseDtoV1;
import chill_logistics.user_server.presentation.dto.ReissueTokenResponseDtoV1;
import chill_logistics.user_server.presentation.dto.SignupRequestDtoV1;
import lib.entity.BaseStatus;
import lib.web.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class AuthControllerV1 {

    private final AuthServiceV1 authService;

    @PostMapping("/login")
    public BaseResponse<LoginResponseDtoV1> login(@RequestBody LoginRequestDtoV1 dto) {

        LoginResultV1 result = authService.login(
                new LoginCommandV1(dto.email(), dto.password())
        );

        return BaseResponse.ok(
                new LoginResponseDtoV1(result.accessToken(), result.refreshToken()),
                BaseStatus.OK
        );
    }

    @PostMapping("/signup")
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

    @PostMapping("/reissue-token")
    public BaseResponse<ReissueTokenResponseDtoV1> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
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
