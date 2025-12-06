package chill_logistics.user_server.application.command;

public record LoginCommandV1(
        String email,
        String password
) { }
