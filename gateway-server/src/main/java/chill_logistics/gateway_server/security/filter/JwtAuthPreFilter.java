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

        // 로그인, 회원가입 등 화이트리스트 경로는 통과
        if (path.startsWith("/v1/user/login") || path.startsWith("/v1/user/signup") || path.startsWith("/v1/user/reissue-token")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // Authorization 추출
        if (authHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Bearer 제거 + 공백 정리
        String token = authHeader.trim();
        if (token.regionMatches(true, 0, "Bearer", 0, 6)) {
            token = token.substring(6).trim();
        }
        token = token.strip();

        // 서명/만료 검증
        if (!jwtTokenProvider.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // accessToken만 들어오게 하고 refreshToken은 막음
        TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
        if (!tokenBody.getType().equals("access")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // master가 관리자인 경우 예시
        if (path.startsWith("/v1/master")){
            if (!tokenBody.getRole().equals(Role.MASTER)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        //Trace-Id 생성
        String traceId = UUID.randomUUID().toString();


        //토큰에서 User-Id 추출
        UUID userUUID = tokenBody.getUserId();
        if (userUUID == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String userId = userUUID.toString();


        // 정규화한 Authorization으로 교체해서 다운스트림으로 전달 + 신뢰 헤더 붙이기도 가능
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
}
