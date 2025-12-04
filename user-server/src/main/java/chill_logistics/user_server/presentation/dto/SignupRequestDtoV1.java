package chill_logistics.user_server.presentation.dto;

public record SignupRequestDtoV1(
        String email,
        String username,
        String nickname,
        String password
) { }
