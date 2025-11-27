package lib.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lib.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JwtTokenProvider  {

    @Value("${custom.jwt.exp-time.access}")
    private long accessTokenTime;

    @Value("${custom.jwt.exp-time.refresh}")
    private long refreshTokenTime;

    @Value("${custom.jwt.secrets.appkey}")
    private String appkey;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(appkey.getBytes(StandardCharsets.UTF_8));
    }


    public String issueAccessToken(UUID id, Role role, String username) {
        String type = "access";
        return issueToken(id, role, accessTokenTime, username, type);
    }


    public String issueRefreshToken(UUID id, Role role, String username) {
        String type = "refresh";
        return issueToken(id, role, refreshTokenTime, username, type);
    }

    private String issueToken(UUID id, Role role, Long expTime, String username, String type) {
        Date now = new Date();
        return Jwts.builder()
            .subject(id.toString())
            .claim("role", role.name())
            .claim("username", username)
            .claim("type", type)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expTime))
            .signWith(getSecretKey(), Jwts.SIG.HS256)
            .compact();
    }


    public boolean validate(String token) {
        try {
            JwtParser jwtParser = Jwts.parser()
                .verifyWith(getSecretKey())
                .build();
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) { log.debug("토큰 만료: {}", maskToken(token)); }
        catch (JwtException e) { log.warn("잘못된 토큰: {}", e.getMessage()); }
        catch (Exception e) { log.error("알 수 없는 오류", e); }
        return false;
    }


    public TokenBody parseJwt(String token) {
        Jws<Claims> parsed = Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token);

        String userId = parsed.getPayload().getSubject();
        String role = parsed.getPayload().get("role", String.class);
        String username = parsed.getPayload().get("username", String.class);
        String type = parsed.getPayload().get("type", String.class);

        return new TokenBody(UUID.fromString(userId), Role.valueOf(role), username, type);
    }

    public Date getExpiration(String token) {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }

    public String maskToken(String token) {
        if (token == null || token.length() < 10) return "(short token)";
        return token.substring(0, 10) + "...(masked)";
    }

}
