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
    HUB_EDGE_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 간 연결(엣지) 정보를 찾을 수 없습니다."),
    KAKAO_DIRECTION_FAILED(HttpStatus.NOT_FOUND, "경로를 찾을 수 없습니다."),

    // 409
    HUB_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 허브명입니다."),

    // 503
    TEMPORARILY_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "외부 지도 API 응답 지연으로 경로 정보를 생성하지 못했습니다. 잠시 후 시도해주세요."),
    KAKAO_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE,"KAKAO 지도 API를 일시적으로 사용 불가합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
