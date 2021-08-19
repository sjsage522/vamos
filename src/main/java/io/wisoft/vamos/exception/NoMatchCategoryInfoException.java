package io.wisoft.vamos.exception;

import io.wisoft.vamos.dto.api.ErrorCode;

public class NoMatchCategoryInfoException extends BusinessException {

    public NoMatchCategoryInfoException() {
        super(ErrorCode.NOMATCH_CATEGORY_INFO);
    }
}
