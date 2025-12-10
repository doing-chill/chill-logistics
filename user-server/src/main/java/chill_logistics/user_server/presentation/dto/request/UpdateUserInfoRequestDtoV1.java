package chill_logistics.user_server.presentation.dto.request;

import chill_logistics.user_server.application.dto.command.UpdateUserInfoCommandV1;

public record UpdateUserInfoRequestDtoV1(

        String email,
        String username,
        String nickname
) {
    public UpdateUserInfoCommandV1 toCommand(){
        return new UpdateUserInfoCommandV1(
                email,
                username,
                nickname
        );
    }
}
