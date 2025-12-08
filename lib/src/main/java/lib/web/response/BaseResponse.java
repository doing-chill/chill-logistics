package lib.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lib.entity.BaseStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private BaseStatus status;
    private T data;

    public BaseResponse() {}

    public BaseResponse(BaseStatus status){this.status = status;}

    public BaseResponse(BaseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    // 응답은 2가지 형태로 나뉩니다.

    // 데이터 반환
    public static <T> BaseResponse<T> ok(T data, BaseStatus status) {
        return new BaseResponse<>(status, data);
    }

    //성공 시 아무것도 반환하고 싶지 않을 때 사용
    public static BaseResponse<Void> ok(BaseStatus status) {
        return new BaseResponse<>(status);
    }



}
