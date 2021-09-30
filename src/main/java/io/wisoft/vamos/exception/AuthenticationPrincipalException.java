package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class AuthenticationPrincipalException extends BusinessException {

    public AuthenticationPrincipalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
