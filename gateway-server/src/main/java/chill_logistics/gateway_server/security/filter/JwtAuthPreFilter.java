package chill_logistics.gateway_server.security.filter;


import java.util.UUID;
import lib.entity.Role;
import lib.jwt.JwtTokenProvider;
import lib.jwt.TokenBody;
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
    private final String TRACE_ID_HEADER = "Trace-Id";
    private final String USER_ID_HEADER = "User-Id";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        log.debug("JwtAuthPreFilter path = {}", path);

        // 1) Swagger / 문서 관련 경로는 전부 화이트리스트
        if (isWhitelisted(path)) {
            log.debug("JwtAuthPreFilter whitelist pass = {}", path);
            return chain.filter(exchange);
        }

        // 2) 로그인/회원가입/토큰 재발급 같은 경로도 화이트리스트
        if (path.startsWith("/v1/users/login")
                || path.startsWith("/v1/users/signup")
                || path.startsWith("/v1/users/reissue-token")) {
            return chain.filter(exchange);
        }

        // === 이 아래부터는 기존 JWT 검사 로직 그대로 ===

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.trim();
        if (token.regionMatches(true, 0, "Bearer", 0, 6)) {
            token = token.substring(6).trim();
        }
        token = token.strip();

        if (!jwtTokenProvider.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
        if (!tokenBody.getType().equals("access")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (path.startsWith("/v1/master")) {
            if (!tokenBody.getRole().equals(Role.MASTER)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        String traceId = UUID.randomUUID().toString();
        UUID userUUID = tokenBody.getUserId();
        if (userUUID == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String userId = userUUID.toString();

        final String cleanToken = token;
        final String traceIdHeaderValue = traceId;
        final String userIdHeaderValue = userId;
        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .headers(h -> {
                    h.set(HttpHeaders.AUTHORIZATION, cleanToken);
                    h.set(TRACE_ID_HEADER, traceIdHeaderValue);
                    h.set(USER_ID_HEADER, userIdHeaderValue);
                })
                .build();
        return chain.filter(exchange.mutate().request(mutated).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
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
