package lib.passport;

public class PassportHeaders {

    private PassportHeaders() {}

    // Passport (내부 인증)
    public static final String PASSPORT_ISSUER = "X-Passport-Issuer";
    public static final String PASSPORT_SERVICE = "X-Passport-Service";
    public static final String PASSPORT_IAT = "X-Passport-Iat";
    public static final String PASSPORT_SIG = "X-Passport-Signature";

    // User context (Gateway가 JWT 검증 후 내려주는 결과)
    public static final String USER_ID = "User-Id";
    public static final String USER_ROLE = "User-Role";
    public static final String USERNAME = "User-Name";
    public static final String TRACE_ID = "Trace-Id";
}
