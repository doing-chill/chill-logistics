package lib.passport;

import lombok.Getter;

@Getter
public class PassportProperties {

    private final String issuer;
    private final String secret;
    private final long allowedSkewMs;

    public PassportProperties(String issuer, String secret, long allowedSkewMs) {
        this.issuer = issuer;
        this.secret = secret;
        this.allowedSkewMs = allowedSkewMs;
    }
}
