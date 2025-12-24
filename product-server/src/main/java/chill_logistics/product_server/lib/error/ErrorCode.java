package chill_logistics.product_server.lib.error;

import lib.web.error.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {

    // 403
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),

    // 409
    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),
    LOCK_INTERRUPTED(HttpStatus.CONFLICT, "락이 방해됐습니다."),
    STOCK_LOCK_FAILED(HttpStatus.CONFLICT, "상품의 락이 실패했습니다."),
    REDIS_STOCK_NOT_INITIALIZED(HttpStatus.CONFLICT, "Redis 재고가 초기화되지 않았습니다."),
    REDIS_STOCK_CORRUPTED(HttpStatus.CONFLICT, "상품 재고 포맷팅에 실패했습니다."),
    STOCK_LOG_FAILED(HttpStatus.CONFLICT, "Redis 재고 로깅을 실패했습니다."),
    REDIS_OPERATION_FAILED(HttpStatus.CONFLICT, "Redis 작업 중 실패했습니다."),
    STOCK_SYNC_FAILED(HttpStatus.CONFLICT, "DB로 재고 동기화 작업 중 실패했습니다."),
    STOCK_LOG_CORRUPTED(HttpStatus.CONFLICT, "재고 로그가 손상되었습니다."),
    DB_STOCK_INCONSISTENT(HttpStatus.CONFLICT, "재고 동기화 중 재고 불일치로 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
