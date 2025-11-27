package chill_logistics.gateway_server.security.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthPostFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
            .doOnSuccess(aVoid -> {
                // 요청 완료 후 처리로 로그 기록, 응답 헤더 추가, 토큰 갱신 요청 등 가능
                System.out.println("Post Filter: 요청 처리 완료");
            });
    }

    @Override
    public int getOrder() {
        return 1; // PreFilter 이후 실행
    }
}
