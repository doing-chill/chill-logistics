package lib.passport;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PassportCrypto {

    private PassportCrypto() {}

    private static final String HMAC_ALG = "HmacSHA256";

    /** 서명 문자열: issuer|service|issuedAt */
    public static String sign(String secret, String issuer, String service, long issuedAt) {
        String data = issuer + "|" + service + "|" + issuedAt;

        try {
            Mac mac = Mac.getInstance(HMAC_ALG);
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALG);
            mac.init(key);

            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        } catch (Exception e) {
            throw new IllegalStateException("Passport sign failed", e);
        }
    }

    public static boolean verify(String secret, String issuer, String service, long issuedAt, String signature) {
        if (signature == null || signature.isBlank()) return false;
        String expected = sign(secret, issuer, service, issuedAt);
        return constantTimeEquals(expected, signature);
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;

        byte[] x = a.getBytes(StandardCharsets.UTF_8);
        byte[] y = b.getBytes(StandardCharsets.UTF_8);

        if (x.length != y.length) return false;

        int diff = 0;
        for (int i = 0; i < x.length; i++) {
            diff |= x[i] ^ y[i];
        }
        return diff == 0;
    }
}
