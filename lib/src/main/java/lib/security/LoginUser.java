package lib.security;

import java.util.UUID;

public record LoginUser(
        UUID id,
        String email,
        String role
) {}
