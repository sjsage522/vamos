package io.wisoft.vamos.controller.api;

import lombok.Getter;

/**
 * 제네릭을 통해 타입 한정
 * @param <T>
 */
@Getter
public class ApiResult<T> {

    private final T data;

    private final String errorMessage;

    private ApiResult(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    /**
     * T 타입의 data 를 갖는 ApiResult 객체 리턴.
     * 성공응답이므로 에러메시지는 null.
     * @param data T 타입 data
     * @param <T> type
     * @return ApiResult
     */
    public static <T> ApiResult<T> succeed(T data) {
        return new ApiResult<>(data, null);
    }

    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<>(null, message);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
