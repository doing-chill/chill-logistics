package lib.web.error;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getStatus();   // HTTP 상태코드
    String getMessage();      // 클라이언트로 보낼 에러 메시지
    String name();

}
