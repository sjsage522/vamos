package io.wisoft.vamos.controller.api.handler;

import io.wisoft.vamos.controller.api.ApiResult;
import io.wisoft.vamos.common.exception.DataAlreadyExistsException;
import io.wisoft.vamos.common.exception.DataNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class RestControllersExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ApiResult<Object> methodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        return ApiResult.failed(ex
                .getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    protected ApiResult<Object> illegalStateException(
            IllegalStateException ex) {
        return ApiResult.failed(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    protected ApiResult<Object> illegalArgumentException(
            IllegalArgumentException ex) {
        return ApiResult.failed(ex.getMessage());
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    @ResponseBody
    protected ApiResult<Object> dataAlreadyExistsException(
            DataAlreadyExistsException ex) {
        return ApiResult.failed(ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    protected ApiResult<Object> dataNotFoundException(
            DataNotFoundException ex) {
        return ApiResult.failed(ex.getMessage());
    }
}
