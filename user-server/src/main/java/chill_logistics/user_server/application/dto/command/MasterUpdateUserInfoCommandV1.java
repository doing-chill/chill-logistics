package chill_logistics.user_server.application.dto.command;

import chill_logistics.user_server.domain.entity.UserRole;

import java.util.UUID;

public record MasterUpdateUserInfoCommandV1(

        UUID userId,
        String email,
        String password,
        String username,
        String nickname,
        UserRole role,
        UUID hubId
) { }
