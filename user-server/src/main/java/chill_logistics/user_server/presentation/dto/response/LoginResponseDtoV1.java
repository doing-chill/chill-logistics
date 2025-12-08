package chill_logistics.user_server.presentation.dto.response;

public record LoginResponseDtoV1(
        String accessToken,
        String refreshToken
) {}