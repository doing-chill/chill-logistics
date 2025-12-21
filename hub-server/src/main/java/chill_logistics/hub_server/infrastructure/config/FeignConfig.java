package chill_logistics.hub_server.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lib.passport.PassportHeaders;
import lib.passport.PassportIssuer;
import lib.passport.ServicePassport;
import lib.passport.ServicePrincipal;
import lib.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@RequiredArgsConstructor
public class FeignConfig {

    private final PassportIssuer passportIssuer;

    @Bean
    public RequestInterceptor passportRequestInterceptor() {
        return new PassportRequestInterceptor(passportIssuer);
    }

    static class PassportRequestInterceptor implements RequestInterceptor {
        private final PassportIssuer passportIssuer;

        PassportRequestInterceptor(PassportIssuer passportIssuer) {
            this.passportIssuer = passportIssuer;
        }

        @Override
        public void apply(RequestTemplate template) {

            ServicePassport passport = passportIssuer.issue("feign");

            template.header(PassportHeaders.PASSPORT_ISSUER, passport.issuer());
            template.header(PassportHeaders.PASSPORT_SERVICE, passport.service());
            template.header(PassportHeaders.PASSPORT_IAT, String.valueOf(passport.issuedAt()));
            template.header(PassportHeaders.PASSPORT_SIG, passport.signature());
            template.header(PassportHeaders.TRACE_ID, UUID.randomUUID().toString());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) return;

            Object principal = auth.getPrincipal();

            LoginUser loginUser = null;
            if (principal instanceof LoginUser lu) loginUser = lu;
            else if (principal instanceof ServicePrincipal sp) loginUser = sp.loginUser();

            if (loginUser == null || loginUser.id() == null || loginUser.role() == null) return;

            template.header(PassportHeaders.USER_ID, loginUser.id().toString());
            template.header(PassportHeaders.USER_ROLE, loginUser.role());
            if (loginUser.email() != null) template.header(PassportHeaders.USERNAME, loginUser.email());
        }
    }
}
