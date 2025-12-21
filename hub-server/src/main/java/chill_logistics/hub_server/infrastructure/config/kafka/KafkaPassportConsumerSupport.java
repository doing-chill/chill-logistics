package chill_logistics.hub_server.infrastructure.config.kafka;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import lib.entity.Role;
import lib.passport.PassportHeaders;
import lib.passport.PassportValidator;
import lib.passport.ServicePassport;
import lib.passport.ServicePrincipal;
import lib.security.LoginUser;
import lib.security.PassportAuthentication;
import org.apache.kafka.common.header.Headers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class KafkaPassportConsumerSupport {

    private KafkaPassportConsumerSupport() {}

    public static void authenticate(Headers headers, PassportValidator validator) {

        String issuer = read(headers, PassportHeaders.PASSPORT_ISSUER);
        String service = read(headers, PassportHeaders.PASSPORT_SERVICE);
        long iat = Long.parseLong(read(headers, PassportHeaders.PASSPORT_IAT));
        String sig = read(headers, PassportHeaders.PASSPORT_SIG);

        ServicePassport passport = new ServicePassport(issuer, service, iat, sig);

        if (!validator.validate(passport)) {
            throw new SecurityException("Invalid passport");
        }

        UUID userId = headers.lastHeader(PassportHeaders.USER_ID) != null
                ? UUID.fromString(read(headers, PassportHeaders.USER_ID))
                : null;

        Role role = headers.lastHeader(PassportHeaders.USER_ROLE) != null
                ? Role.valueOf(read(headers, PassportHeaders.USER_ROLE))
                : null;

        String username = headers.lastHeader(PassportHeaders.USERNAME) != null
                ? read(headers, PassportHeaders.USERNAME)
                : null;

        LoginUser loginUser = (userId != null && role != null)
                ? new LoginUser(userId, username, role.name())
                : null;

        ServicePrincipal principal = new ServicePrincipal(service, loginUser);

        SecurityContextHolder.getContext().setAuthentication(
                new PassportAuthentication(
                        principal,
                        role != null
                                ? List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
                                : List.of()
                )
        );
    }

    public static void clear() {
        SecurityContextHolder.clearContext();
    }

    private static String read(Headers headers, String key) {
        var h = headers.lastHeader(key);
        if (h == null) throw new SecurityException("Missing header: " + key);
        return new String(h.value(), StandardCharsets.UTF_8);
    }
}
