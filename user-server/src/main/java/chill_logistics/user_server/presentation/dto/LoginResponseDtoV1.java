package chill_logistics.user_server.presentation.dto;

public record LoginResponseDtoV1(
        String accessToken,
        String refreshToken
) {}