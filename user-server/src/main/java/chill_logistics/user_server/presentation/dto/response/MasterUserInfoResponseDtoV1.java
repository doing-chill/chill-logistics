package chill_logistics.user_server.presentation.dto.response;

import chill_logistics.user_server.application.dto.query.MasterUserInfoQueryV1;
import chill_logistics.user_server.domain.entity.UserRole;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MasterUserInfoResponseDtoV1(
        String email,
        String username,
        String nickname,
        UserRole role,
        UUID hubId,
        String hubName
) {

    public static MasterUserInfoResponseDtoV1 from(MasterUserInfoQueryV1 query) {
        return MasterUserInfoResponseDtoV1.builder()
                .email(query.email())
                .username(query.username())
                .nickname(query.nickname())
                .role(query.role())
                .hubId(query.hubId())
                .hubName(query.hubName())
                .build();
    }
}
