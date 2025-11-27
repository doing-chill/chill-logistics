package chill_logistics.gateway_server.security.application.event;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Order(-2)
public class GatewayProblemHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange ex, Throwable t) {
        var res = ex.getResponse();

        // 1) 이미 커밋된 응답엔 바디를 쓸 수 없음 (다운스트림 응답이 있는 경우 핸들러른 동작하지 않게)
        if (res.isCommitted()) {
            return Mono.error(t);
        }

        // 2) 게이트웨이 내부 예외만 표준화 (다운스트림 에러는 보통 여기 안 옴)
        HttpStatus status = map(t);

        res.setStatusCode(status);
        res.getHeaders().setContentType(MediaType.valueOf("application/problem+json"));

        String traceId = ex.getRequest().getHeaders().getFirst("X-Request-Id");
        if (traceId == null) traceId = ex.getLogPrefix(); // fallback

        String body = """
          {
            "type": "https://example.com/problems/GATEWAY_%s",
            "title": "%s",
            "status": %d,
            "detail": "%s",
            "code": "GATEWAY_%s",
            "traceId": "%s",
            "instance": "%s"
          }
        """.formatted(
            status.name(),
            status.getReasonPhrase(),
            status.value(),
            sanitize(t.getMessage()),
            status.name(),
            sanitize(traceId),
            ex.getRequest().getPath().value()
        );

        DataBuffer buf = res.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return res.writeWith(Mono.just(buf));
    }

    /** 게이트웨이에서 자주 나는 예외 → 상태코드 매핑 */
    private HttpStatus map(Throwable t) {
        if (t instanceof NotFoundException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        if (t instanceof ResponseStatusException rse) {
            HttpStatus s = HttpStatus.resolve(rse.getStatusCode().value());
            return (s != null ? s : HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (t instanceof TimeoutException
            || t instanceof ReadTimeoutException
            || t instanceof ConnectTimeoutException) {
            return HttpStatus.GATEWAY_TIMEOUT; // 504
        }
        if (t instanceof SecurityException) {
            return HttpStatus.UNAUTHORIZED; // 필요시 403
        }
        return HttpStatus.INTERNAL_SERVER_ERROR; // 500
    }

    /** 로그/응답에 넣기 안전하도록 간단 정제 */
    private String sanitize(String s) {
        if (s == null) return "";
        return s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
            .replace("\"", "'")
            .trim();
    }
}