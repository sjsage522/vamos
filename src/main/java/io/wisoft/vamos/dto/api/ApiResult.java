package io.wisoft.vamos.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 제네릭을 통해 타입 한정
 * @param <T>
 */
@Getter
@ApiModel("응답 포맷")
public class ApiResult<T> {

    @ApiModelProperty(
            value = "정상 응답 데이터",
            name = "data"
    )
    private final T data;

    @ApiModelProperty(
            value = "에러 응답 데이터",
            name = "error"
    )
    @JsonProperty("error")
    private final ErrorResponse errorTemplate;

    private ApiResult(T data, ErrorResponse errorTemplate) {
        this.data = data;
        this.errorTemplate = errorTemplate;
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

    public static <T> ApiResult<T> failed(ErrorResponse errorTemplate) {
        return new ApiResult<>(null, errorTemplate);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "data=" + data +
                ", errorTemplate=" + errorTemplate +
                '}';
    }
}
