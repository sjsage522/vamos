package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NotYetSettingUserLocationException extends BusinessException {

    public NotYetSettingUserLocationException() {
        super(ErrorCode.NOT_SET_USER_LOCATION);
    }
}
