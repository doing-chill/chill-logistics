package lib.passport;

public record ServicePassport(

        String issuer,
        String service,
        long issuedAt,
        String signature
) {
}
