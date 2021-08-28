package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NoMatchChattingRoomException extends BusinessException {

    public NoMatchChattingRoomException() {
        super(ErrorCode.NOMATCH_CHAT_ROOM_INFO);
    }
}
