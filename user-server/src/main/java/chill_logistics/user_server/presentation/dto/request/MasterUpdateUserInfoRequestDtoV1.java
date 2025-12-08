package chill_logistics.user_server.presentation.dto.request;

import chill_logistics.user_server.application.dto.command.MasterUpdateUserInfoCommandV1;
import chill_logistics.user_server.domain.entity.UserRole;

import java.util.UUID;

public record MasterUpdateUserInfoRequestDtoV1(

        String email,
        String password,
        String username,
        String nickname,
        UserRole role,
        UUID hubId
) {

    public MasterUpdateUserInfoCommandV1 toCommand(UUID userId) {
        return new MasterUpdateUserInfoCommandV1(
                userId,
                email,
                password,
                username,
                nickname,
                role,
                hubId
        );
    }
}

