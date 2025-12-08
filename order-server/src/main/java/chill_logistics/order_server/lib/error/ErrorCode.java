package chill_logistics.order_server.lib.error;

import lib.web.error.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {

    // 400
    PRODUCT_NOT_FROM_FIRM(HttpStatus.BAD_REQUEST, "상품이 해당 업체에 속하지 않습니다."),

    // 403
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),

    // 409
    INVALID_ORDER_STATUS_TRANSITION(HttpStatus.CONFLICT, "해당 주문 상태로 변경할 수 없습니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
