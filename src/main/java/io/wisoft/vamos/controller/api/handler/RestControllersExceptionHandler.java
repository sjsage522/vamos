package io.wisoft.vamos.controller.api.handler;

import io.wisoft.vamos.common.exception.DataAlreadyExistsException;
import io.wisoft.vamos.common.exception.DataNotFoundException;
import io.wisoft.vamos.controller.api.ApiResult;
import io.wisoft.vamos.controller.api.ErrorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
@Slf4j
public class RestControllersExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    protected ApiResult<Object> runtimeException(
            Exception ex) {
        log.info("interval server error message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    protected ApiResult<Object> methodNotAllowedException(
            HttpRequestMethodNotSupportedException ex) {
        log.info("method not allowed error message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), METHOD_NOT_ALLOWED.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    protected ApiResult<Object> accessDeniedException(
            AccessDeniedException ex) {
        log.info("forbidden error message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), FORBIDDEN.value()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    protected ApiResult<Object> noHandlerFoundException(
            NoHandlerFoundException ex) {
        log.info("not found error message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), NOT_FOUND.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> methodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.info("bad request message = {}",ex.getMessage());
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
        log.info("bad request message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> illegalArgumentException(
            IllegalArgumentException ex) {
        log.info("bad request message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> dataAlreadyExistsException(
            DataAlreadyExistsException ex) {
        log.info("bad request message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    protected ApiResult<Object> dataNotFoundException(
            DataNotFoundException ex) {
        log.info("bad request message = {}",ex.getMessage());
        return failed(ErrorTemplate.from(ex.getMessage(), BAD_REQUEST.value()));
    }
}
