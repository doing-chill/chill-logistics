package chill_logistics.user_server.application.dto.query;

import chill_logistics.user_server.domain.entity.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MasterUserInfoListQueryV1(
        UUID id,
        String email,
        String username,
        String nickname,
        UserRole role,
        UUID hubId,
        String hubName
) { }
