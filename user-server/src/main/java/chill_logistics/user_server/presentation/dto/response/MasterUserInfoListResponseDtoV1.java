package chill_logistics.user_server.presentation.dto.response;

import chill_logistics.user_server.application.dto.query.MasterUserInfoListQueryV1;
import chill_logistics.user_server.domain.entity.UserRole;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record MasterUserInfoListResponseDtoV1(
        UUID id,
        String email,
        String password,
        String username,
        String nickname,
        UserRole role,
        UUID hubId,
        String hubName
) {
    public static MasterUserInfoListResponseDtoV1 from(MasterUserInfoListQueryV1 query) {
        return MasterUserInfoListResponseDtoV1.builder()
                .id(query.id())
                .email(query.email())
                .password(query.password())
                .username(query.username())
                .nickname(query.nickname())
                .role(query.role())
                .hubId(query.hubId())
                .hubName(query.hubName())
                .build();
    }

    public static List<MasterUserInfoListResponseDtoV1> from(List<MasterUserInfoListQueryV1> queries) {
        return queries.stream()
                .map(MasterUserInfoListResponseDtoV1::from)
                .toList();
    }
}
