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
    ORDER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 주문 ID가 없습니다."),
    ORDER_OUTBOX_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 OUTBOX 이벤트를 찾을 수 없습니다."),

    // 409
    INVALID_ORDER_STATUS_TRANSITION(HttpStatus.CONFLICT, "해당 주문 상태로 변경할 수 없습니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),
    ORDER_NOT_IN_MANAGING_HUB(HttpStatus.CONFLICT, "담당하는 허브 소속의 주문이 아닙니다."),
    ORDER_NOT_CREATED_BY_USER(HttpStatus.CONFLICT, "해당 주문은 본인 주문이 아닙니다."),
    IDEMPOTENCY_ALREADY_IN_PROGRESS(HttpStatus.CONFLICT, "다른 요청이 이미 진행중입니다. (중복된 요청)"),
    FAIlED_IDEMPOTENCY(HttpStatus.CONFLICT, "주문 요청이 실패했습니다."),
    UNKNOWN_EVENT_TYPE(HttpStatus.CONFLICT, "EventType 해당되는 Topic이 없습니다."),
    OUTBOX_PAYLOAD_SERIALIZATION_FAILED(HttpStatus.CONFLICT, "OUTBOX payload 직렬화에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
