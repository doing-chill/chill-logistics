package chill_logistics.firm_server.application.dto.response;

import lib.entity.Role;

public record UserResponseV1 (

    Role role,
    String username,
    String nickname,
    String email

){}
