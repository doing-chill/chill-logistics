package chill_logistics.delivery_server.presentation;

import lib.web.error.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {

    // 404
    HUB_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 배송 정보를 찾을 수 없습니다."),
    FIRM_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 배송 정보를 찾을 수 없습니다."),

    // 400
    INVALID_CHANGE_DELIVERY_STATUS(HttpStatus.BAD_REQUEST, "허용되지 않는 배송 상태 변경입니다."),
    DELIVERY_ALREADY_COMPLETED_OR_CANCELED(HttpStatus.BAD_REQUEST, "이미 완료되었거나 취소된 배송입니다."),
    DELIVERY_HAS_BEEN_DELETED(HttpStatus.BAD_REQUEST, "삭제된 배송입니다."),
    INVALID_HUB_DELIVERY_PERSON(HttpStatus.BAD_REQUEST, "허브 배송 담당자가 아닙니다."),
    INVALID_FIRM_DELIVERY_PERSON(HttpStatus.BAD_REQUEST, "업체 배송 담당자가 아닙니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
