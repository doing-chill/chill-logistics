package chill_logistics.user_server.application.command;

public record LoginResultV1(
        String accessToken,
        String refreshToken
) { }
