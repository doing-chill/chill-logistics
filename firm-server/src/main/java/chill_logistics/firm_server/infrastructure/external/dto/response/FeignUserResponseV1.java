package chill_logistics.firm_server.infrastructure.external.dto.response;

import chill_logistics.firm_server.application.dto.response.UserResponseV1;
import lib.entity.Role;

public record FeignUserResponseV1(
    Role role,
    String username,
    String nickname,
    String email
){
    public UserResponseV1 to(){
        return new UserResponseV1(this.role, this.username, this.nickname, this.email);
    }
}
