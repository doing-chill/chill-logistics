package chill_logistics.gateway_server.security.filter;


import java.util.UUID;
import lib.entity.Role;
import lib.jwt.JwtTokenProvider;
import lib.jwt.TokenBody;
import lib.passport.PassportHeaders;
import lib.passport.PassportIssuer;
import lib.passport.ServicePassport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthPreFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private final PassportIssuer passportIssuer;
    private final String TRACE_ID_HEADER = "Trace-Id";
    private final String USER_ID_HEADER = "User-Id";

    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("### JwtAuthPreFilter LOADED ###");
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("### JwtAuthPreFilter HIT ### path=" + exchange.getRequest().getURI().getPath());

        String path = exchange.getRequest().getPath().toString();

        if (isWhitelisted(path)) {
            return forwardWithPassportOnly(exchange, chain);
        }

        if (isPublicAuthEndpoint(path)) {
            return forwardWithPassportOnly(exchange, chain);
        }

        log.info("Gateway path = {}", exchange.getRequest().getURI().getPath());

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.trim();
        if (token.regionMatches(true, 0, "Bearer", 0, 6)) token = token.substring(6).trim();
        token = token.strip();

        if (!jwtTokenProvider.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
        if (!"access".equals(tokenBody.getType())) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String traceId = UUID.randomUUID().toString();
        String userId = tokenBody.getUserId().toString();
        String userRole = tokenBody.getRole().name();
        String username = tokenBody.getUsername(); // optional

        // ✅ Passport 발급 (callerService를 "gateway"로 시작)
        ServicePassport passport = passportIssuer.issue("gateway");

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .headers(h -> {
                    // ❌ 내부로 JWT 전달 금지
                    h.remove(HttpHeaders.AUTHORIZATION);

                    // ✅ Passport
                    h.set(PassportHeaders.PASSPORT_ISSUER, passport.issuer());
                    h.set(PassportHeaders.PASSPORT_SERVICE, passport.service());
                    h.set(PassportHeaders.PASSPORT_IAT, String.valueOf(passport.issuedAt()));
                    h.set(PassportHeaders.PASSPORT_SIG, passport.signature());

                    // ✅ UserContext (Gateway가 JWT 검증 후 결과 전달)
                    h.set(PassportHeaders.TRACE_ID, traceId);
                    h.set(PassportHeaders.USER_ID, userId);
                    h.set(PassportHeaders.USER_ROLE, userRole);
                    if (username != null) h.set(PassportHeaders.USERNAME, username);
                })
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    @Override
    public int getOrder() { return Ordered.HIGHEST_PRECEDENCE; }

    private boolean isPublicAuthEndpoint(String path) {
        return path.startsWith("/v1/users/login")
                || path.startsWith("/user-server/v1/users/login")
                || path.startsWith("/v1/users/signup")
                || path.startsWith("/user-server/v1/users/signup")
                || path.startsWith("/v1/users/reissue-token")
                || path.startsWith("/user-server/v1/users/reissue-token");
    }

    private Mono<Void> forwardWithPassportOnly(ServerWebExchange exchange, GatewayFilterChain chain) {

        String traceId = UUID.randomUUID().toString();
        ServicePassport passport = passportIssuer.issue("gateway");

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .headers(h -> {
                    // 내부로 JWT 전달 금지
                    h.remove(HttpHeaders.AUTHORIZATION);

                    // ✅ Passport만 주입
                    h.set(PassportHeaders.PASSPORT_ISSUER, passport.issuer());
                    h.set(PassportHeaders.PASSPORT_SERVICE, passport.service());
                    h.set(PassportHeaders.PASSPORT_IAT, String.valueOf(passport.issuedAt()));
                    h.set(PassportHeaders.PASSPORT_SIG, passport.signature());

                    // trace는 주입
                    h.set(PassportHeaders.TRACE_ID, traceId);

                    // ✅ public endpoint는 유저컨텍스트 없음
                    h.remove(PassportHeaders.USER_ID);
                    h.remove(PassportHeaders.USER_ROLE);
                    h.remove(PassportHeaders.USERNAME);
                })
                .build();

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private boolean isWhitelisted(String path) {
        // Swagger UI
        if (path.equals("/swagger-ui.html") || path.startsWith("/swagger-ui")) {
            return true;
        }

        // Gateway 자체 OpenAPI
        if (path.equals("/v3/api-docs") || path.startsWith("/v3/api-docs")) {
            return true;
        }

        // 각 도메인 OpenAPI 프록시 (gateway 라우트랑 동일하게)
        if (path.equals("/user-server/v3/api-docs")
                || path.equals("/order-server/v3/api-docs")
                || path.equals("/product-server/v3/api-docs")
                || path.equals("/firm-server/v3/api-docs")
                || path.equals("/hub-server/v3/api-docs")
                || path.equals("/delivery-server/v3/api-docs")
                || path.equals("/external-server/v3/api-docs")) {
            return true;
        }

        // 필요하면 actuator 등 추가
        return false;
    }
}
