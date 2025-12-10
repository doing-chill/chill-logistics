package chill_logistics.firm_server.application.dto.response;

import java.util.UUID;
import lib.entity.Role;

public record UserResponseV1 (

    String email,
    String username,
    String nickname,
    Role role,
    UUID hubId,
    String hubName

){}
