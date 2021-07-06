package io.wisoft.vamos.controller.api.handler;

import io.wisoft.vamos.common.exception.DataAlreadyExistsException;
import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.controller.api.ApiResult;
import io.wisoft.vamos.controller.api.ErrorTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static io.wisoft.vamos.controller.api.ApiResult.failed;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@ResponseBody
public class RestControllersExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ApiResult<Object> runtimeException(
            Exception ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    protected ApiResult<Object> accessDeniedException(
            AccessDeniedException ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), FORBIDDEN.value()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiResult<Object> noHandlerFoundException(
            NoHandlerFoundException ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), NOT_FOUND.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> methodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        return failed(
                ErrorTemplate.from(ex
                        .getBindingResult()
                        .getAllErrors()
                        .get(0)
                        .getDefaultMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> illegalStateException(
            IllegalStateException ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> illegalArgumentException(
            IllegalArgumentException ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> dataAlreadyExistsException(
            DataAlreadyExistsException ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> dataNotFoundException(
            DataNotFoundException ex) {
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }
}
