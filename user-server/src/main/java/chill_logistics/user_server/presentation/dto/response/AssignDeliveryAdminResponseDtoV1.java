package chill_logistics.user_server.presentation.dto.response;

import java.util.UUID;

public record AssignDeliveryAdminResponseDtoV1(
        UUID userId,
        String username
) { }
