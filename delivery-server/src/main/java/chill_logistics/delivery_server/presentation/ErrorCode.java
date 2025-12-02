package chill_logistics.delivery_server.presentation;

import lib.web.error.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {

    // 404
    HUB_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 배송 정보를 찾을 수 없습니다."),
    FIRM_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 배송 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
