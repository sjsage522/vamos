package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NoMatchBoardInfoException extends BusinessException {

    public NoMatchBoardInfoException() {
        super(ErrorCode.NOMATCH_BOARD_INFO);
    }

    public NoMatchBoardInfoException(String message) {
        super(message, ErrorCode.NOMATCH_BOARD_INFO);
    }
}
