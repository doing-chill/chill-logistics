package chill_logistics.user_server.presentation.dto;

public record ReissueTokenResponseDtoV1(
        String accessToken,
        String refreshToken
) { }
