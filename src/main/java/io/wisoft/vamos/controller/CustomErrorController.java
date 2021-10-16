package io.wisoft.vamos.controller;

import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.api.ErrorCode;
import io.wisoft.vamos.dto.api.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ResponseEntity<ApiResult<ErrorResponse>> handleError(HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.from(
                ErrorCode.HANDLER_NOT_FOUND
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResult.failed(response));
    }
}
