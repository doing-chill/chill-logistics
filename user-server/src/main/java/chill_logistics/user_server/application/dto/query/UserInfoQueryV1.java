package chill_logistics.user_server.application.dto.query;

import chill_logistics.user_server.domain.entity.UserRole;

public record UserInfoQueryV1(
        String email,
        String username,
        String nickname,
        UserRole role
) { }
