package lib.audit;

import lib.passport.ServicePrincipal;
import lib.security.LoginUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class CommonAuditorAware implements AuditorAware<UUID> {

    private static final UUID SYSTEM =
            UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.of(SYSTEM);
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof LoginUser loginUser && loginUser.id() != null) {
            return Optional.of(loginUser.id());
        }

        if (principal instanceof ServicePrincipal sp && sp.loginUser() != null && sp.loginUser().id() != null) {
            return Optional.of(sp.loginUser().id());
        }

        return Optional.of(SYSTEM);
    }
}
