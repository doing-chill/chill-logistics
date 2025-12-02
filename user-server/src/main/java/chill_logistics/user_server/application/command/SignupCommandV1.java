package chill_logistics.user_server.application.command;

public record SignupCommandV1(
        String email,
        String username,
        String nickname,
        String password
) { }
