package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NoMatchUserInfoException extends BusinessException {

    public NoMatchUserInfoException() { super(ErrorCode.NOMATCH_USER_INFO); }
}
