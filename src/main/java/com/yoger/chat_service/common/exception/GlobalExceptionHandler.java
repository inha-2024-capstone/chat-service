package com.yoger.chat_service.common.exception;

import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.enums.FailureStatus;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseBody<?>> userExHandler(Exception exception) {
        log.debug(exception.getMessage());
        log.debug(Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(FailureStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(FailureStatus.INTERNAL_SERVER_ERROR.getResponseBody(exception.getMessage()));
    }
}
