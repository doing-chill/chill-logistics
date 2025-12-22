package chill_logistics.order_server.infrastructure.kafka;

import lib.passport.PassportHeaders;
import lib.passport.PassportIssuer;
import lib.passport.ServicePassport;
import lib.util.SecurityUtils;
import org.apache.kafka.common.header.Headers;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class KafkaPassportProducerSupport {

    private KafkaPassportProducerSupport() {}

    public static void writeHeaders(
            Headers headers,
            PassportIssuer passportIssuer
    ) {
        ServicePassport passport = passportIssuer.issue("kafka");

        put(headers, PassportHeaders.PASSPORT_ISSUER, passport.issuer());
        put(headers, PassportHeaders.PASSPORT_SERVICE, passport.service());
        put(headers, PassportHeaders.PASSPORT_IAT, String.valueOf(passport.issuedAt()));
        put(headers, PassportHeaders.PASSPORT_SIG, passport.signature());

        put(headers, PassportHeaders.TRACE_ID, UUID.randomUUID().toString());

        SecurityUtils.getCurrentUser().ifPresent(u -> {
            put(headers, PassportHeaders.USER_ID, u.id().toString());
            put(headers, PassportHeaders.USER_ROLE, u.role());
            if (u.email() != null) {
                put(headers, PassportHeaders.USERNAME, u.email());
            }
        });
    }

    private static void put(Headers headers, String key, String value) {
        headers.remove(key);
        headers.add(key, value.getBytes(StandardCharsets.UTF_8));
    }
}
