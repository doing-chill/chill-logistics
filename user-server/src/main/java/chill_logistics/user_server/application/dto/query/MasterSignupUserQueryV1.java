package chill_logistics.user_server.application.dto.query;

import chill_logistics.user_server.domain.entity.SignupStatus;
import chill_logistics.user_server.domain.entity.UserRole;
import chill_logistics.user_server.presentation.dto.response.MasterSignupUserResponseDtoV1;

import java.util.UUID;

public record MasterSignupUserQueryV1(
        UUID id,
        String email,
        String username,
        String nickname,
        UserRole role,
        SignupStatus signupStatus,
        String hubName
) { }
