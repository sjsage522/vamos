package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NoMatchCommentInfoException extends BusinessException {

    public NoMatchCommentInfoException() {
        super(ErrorCode.NOMATCH_COMMENT_INFO);
    }

    public NoMatchCommentInfoException(String message) { super(message, ErrorCode.NOMATCH_COMMENT_INFO); }
}
