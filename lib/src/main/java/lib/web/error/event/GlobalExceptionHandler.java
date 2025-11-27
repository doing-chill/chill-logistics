package lib.web.error.event;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lib.web.error.BaseErrorCode;
import lib.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lib.web.error.BusinessException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        BaseErrorCode code = e.getErrorCode();
        String path = request.getMethod() + " " + request.getRequestURI();
        return ResponseEntity.status(code.getStatus()).body(ErrorResponse.builder()
                .status(code.getStatus().value())
                .code(code.name())
                .message(code.getMessage())
                .path(path)
                .build());
    }
    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("INVALID_ARGUMENT")
                .message(e.getMessage())
                .path(path)
                .build());
    }
    // EntityNotFoundException 처리 (JPA 프록시 객체 관련)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code("ENTITY_NOT_FOUND")
                .message(e.getMessage() != null ? e.getMessage() : "요청한 리소스를 찾을 수 없습니다.")
                .path(path)
                .build());
    }
    // Validation 예외 처리 (@Valid 실패 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("유효성 검증에 실패했습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("VALIDATION_FAILED")
                .message(message)
                .path(path)
                .build());
    }
    // 타입 불일치 예외 처리 (UUID 파싱 실패 등)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("TYPE_MISMATCH")
                .message("잘못된 형식의 데이터입니다: " + e.getName())
                .path(path)
                .build());
    }
    // 기타 모든 예외 처리 (최종 방어선)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        String path = request.getMethod() + " " + request.getRequestURI();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("INTERNAL_SERVER_ERROR")
                .message("서버 내부 오류가 발생했습니다.")
                .path(path)
                .build());
    }
}