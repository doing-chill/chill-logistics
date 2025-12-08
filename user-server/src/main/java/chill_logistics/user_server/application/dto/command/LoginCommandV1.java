package chill_logistics.user_server.application.dto.command;

public record LoginCommandV1(
        String email,
        String password
) { }
