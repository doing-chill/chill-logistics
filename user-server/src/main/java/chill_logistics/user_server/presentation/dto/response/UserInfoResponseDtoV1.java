package chill_logistics.user_server.presentation.dto.response;

import chill_logistics.user_server.application.dto.query.UserInfoQueryV1;
import chill_logistics.user_server.domain.entity.UserRole;

public record UserInfoResponseDtoV1(
        String email,
        String username,
        String nickname,
        UserRole role
) {

    public static UserInfoResponseDtoV1 from(UserInfoQueryV1 query) {
        return new UserInfoResponseDtoV1(
                query.email(),
                query.username(),
                query.nickname(),
                query.role()
        );
    }
}
