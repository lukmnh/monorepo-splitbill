package com.splitbill.common.exception;

import com.splitbill.common.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> BusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(
                        errorCode.getCode(),
                        ex.getMessage(),
                        errorCode.getHttpStatus().value()
                ));
    }

    @Getter
    public static class ErrorResponse {
        private final String code;
        private final String message;
        private final int status;
        private final LocalDateTime timestamp;

        public ErrorResponse(String code, String message, int status) {
            this.code = code;
            this.message = message;
            this.status = status;
            this.timestamp = LocalDateTime.now();
        }
    }
}
