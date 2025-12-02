package chill_logistics.hub_server.infrastructure.external.dto.response;

import lib.entity.Role;

public record UserResponseV1(
    Role role,
    String username,
    String nickname,
    String email
){}
