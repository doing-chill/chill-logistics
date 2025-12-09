package chill_logistics.user_server.presentation.dto.request;

import chill_logistics.user_server.application.dto.command.MasterUpdateSignupStatusCommandV1;

import java.util.UUID;

public record MasterUpdateSignupStatusRequestDtoV1(
        String signupStatus
) {
    public MasterUpdateSignupStatusCommandV1 toCommand(UUID userId) {
        return new MasterUpdateSignupStatusCommandV1(
                userId,
                signupStatus
        );
    }
}
