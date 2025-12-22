package lib.passport;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PassportValidator {

    private final PassportProperties props;

    public boolean validate(ServicePassport passport) {
        if (passport == null) return false;

        // issuer 확인
        if (!props.getIssuer().equals(passport.issuer())) return false;

        // 시간차(리플레이 완화)
        long now = System.currentTimeMillis();
        long skew = Math.abs(now - passport.issuedAt());
        if (skew > props.getAllowedSkewMs()) return false;

        // 서명 검증
        return PassportCrypto.verify(
                props.getSecret(),
                passport.issuer(),
                passport.service(),
                passport.issuedAt(),
                passport.signature()
        );
    }
}
