package lib.passport;

import lib.security.LoginUser;

public record ServicePrincipal(
        String callerService,
        LoginUser loginUser
) {}
