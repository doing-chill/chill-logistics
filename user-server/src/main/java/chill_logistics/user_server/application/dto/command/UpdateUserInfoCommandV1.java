package chill_logistics.user_server.application.dto.command;

public record UpdateUserInfoCommandV1(

        String email,
        String username,
        String nickname
) { }
