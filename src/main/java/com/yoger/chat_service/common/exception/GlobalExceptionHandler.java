package com.yoger.chat_service.common.exception;

import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.enums.FailureStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseBody<?>> userExHandler(Exception exception) {
        log.info(exception.getMessage());
        return ResponseEntity.status(FailureStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(FailureStatus.INTERNAL_SERVER_ERROR.getBaseResponseBody(exception.getMessage()));
    }
}
