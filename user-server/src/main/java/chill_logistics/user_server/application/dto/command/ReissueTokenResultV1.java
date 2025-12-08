package chill_logistics.user_server.application.dto.command;

public record ReissueTokenResultV1(
        String accessToken,
        String refreshToken
) { }
