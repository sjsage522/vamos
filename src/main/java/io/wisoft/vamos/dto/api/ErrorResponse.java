package io.wisoft.vamos.dto.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@ApiModel("에러 응답 포맷")
public class ErrorResponse {

    @ApiModelProperty(
            value = "에러 응답 메시지",
            name = "message",
            example = "404 NOT FOUND"
    )
    private final String message;

    @ApiModelProperty(
            value = "에러 응답 상세코드",
            name = "code",
            example = "C001"
    )
    private final String code;

    @ApiModelProperty(
            value = "에러 응답 상태코드",
            name = "status",
            example = "404"
    )
    private final int status;

    private ErrorResponse(final ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
    }

    private ErrorResponse(final String message, final int status, final String code) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public static ErrorResponse from(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(final String message, final ErrorCode errorCode) {
        return new ErrorResponse(message, errorCode.getStatus(), errorCode.getCode());
    }

    public static ErrorResponse of(final BindingResult bindingResult, final ErrorCode errorCode) {
        final String message = bindingResult
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        final int status = errorCode.getStatus();

        return new ErrorResponse(message, status, errorCode.getCode());
    }
}
