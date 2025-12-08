package chill_logistics.user_server.presentation.dto.response;

import chill_logistics.user_server.application.dto.query.MasterSignupUserQueryV1;
import chill_logistics.user_server.domain.entity.SignupStatus;
import chill_logistics.user_server.domain.entity.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record MasterSignupUserResponseDtoV1(
        UUID id,
        String email,
        String username,
        String nickname,
        UserRole role,
        SignupStatus signupStatus,
        String hubName
) {
    public static List<MasterSignupUserResponseDtoV1> from(List<MasterSignupUserQueryV1> queries) {

        List<MasterSignupUserResponseDtoV1> masterSignupUserResponse = new ArrayList<>();

        for(MasterSignupUserQueryV1 query : queries) {
            masterSignupUserResponse.add(new MasterSignupUserResponseDtoV1(
                    query.id(),
                    query.email(),
                    query.username(),
                    query.nickname(),
                    query.role(),
                    query.signupStatus(),
                    query.hubName()
            ));
        }

        return masterSignupUserResponse;
    }
}
