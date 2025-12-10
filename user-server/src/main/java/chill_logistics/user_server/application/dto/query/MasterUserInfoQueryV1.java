package chill_logistics.user_server.application.dto.query;

import chill_logistics.user_server.domain.entity.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MasterUserInfoQueryV1(
        String email,
        String password,
        String username,
        String nickname,
        UserRole role,
        UUID hubId,
        String hubName
) { }
