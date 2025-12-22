package lib.passport;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PassportIssuer {

    private final PassportProperties props;

    public ServicePassport issue(String serviceName) {

        long now = System.currentTimeMillis();
        String issuer = props.getIssuer();
        String sig = PassportCrypto.sign(props.getSecret(), issuer, serviceName, now);

        // 이미 만든 ServicePassport record(필드 순서) 맞춰서 생성
        return new ServicePassport(issuer, serviceName, now, sig);
    }
}
