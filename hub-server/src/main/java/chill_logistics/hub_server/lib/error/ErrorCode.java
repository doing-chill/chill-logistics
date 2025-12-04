package chill_logistics.hub_server.lib.error;

import lib.web.error.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {

    // 403
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
    HUB_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 연결 정보를 찾을 수 없습니다."),
    HUB_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 경로 정보를 찾을 수 없습니다."),
    HUB_INFO_START_EQUALS_END(HttpStatus.CONFLICT, "출발 허브와 도착 허브는 동일할 수 없습니다."),

    // 409
    HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 허브명입니다."),

    KAKAO_SERVER_ERROR(HttpStatus.BAD_REQUEST, "카카오 요청 API가 동작하지 않습니다")

    ;


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
