package chill_logistics.hub_server.infrastructure.external.dto.response;


import chill_logistics.hub_server.application.dto.response.UserResponseV1;
import java.util.UUID;
import lib.entity.Role;

public record FeignUserResponseV1(
    String email,
    String username,
    String nickname,
    Role role,
    UUID hubId,
    String hubName
){
    public UserResponseV1 to(){
        return new UserResponseV1(
            this.email, this.username, this.nickname, this.role, this.hubId, this.hubName);
    }
}
