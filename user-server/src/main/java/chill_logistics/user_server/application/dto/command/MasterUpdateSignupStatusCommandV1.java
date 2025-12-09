package chill_logistics.user_server.application.dto.command;

import java.util.UUID;

public record MasterUpdateSignupStatusCommandV1(
        UUID userId,
        String signupStatus
) { }
