package lib.jwt;

import java.util.UUID;
import lib.entity.Role;
import lombok.Getter;


@Getter
public class TokenBody {
    private UUID userId;
    private Role role;
    private String username;
    // AccessToken인지 RefreshToken인지 type
    private String type;

    public TokenBody(UUID userId, Role role, String username, String type) {
        this.userId = userId;
        this.role = role;
        this.username = username;
        this.type = type;
    }
}
