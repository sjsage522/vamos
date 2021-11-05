package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NotYetSettingUserNicknameException extends BusinessException {

    public NotYetSettingUserNicknameException() {
        super(ErrorCode.NOT_SET_USER_NICKNAME);
    }
}
