package lib.audit;

import lib.jwt.TokenBody;
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

        // 1) JWT 기반 TokenBody
        if (principal instanceof TokenBody tokenBody) {
            if (tokenBody.getUserId() != null) {
                return Optional.of(tokenBody.getUserId());
            }
        }

        // 2) LoginUser 기반 (다른 구조에서 쓸 수도 있음)
        if (principal instanceof LoginUser loginUser) {
            if (loginUser.id() != null) {
                return Optional.of(loginUser.id());
            }
        }

        // 3) 그 외는 null 값으로 오류 처리
        return Optional.empty();
    }
}
