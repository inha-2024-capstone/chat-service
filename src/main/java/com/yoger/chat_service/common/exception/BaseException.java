package com.yoger.chat_service.common.exception;

import com.yoger.chat_service.common.status.enums.FailureStatus;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final FailureStatus failureStatus;

    public BaseException(FailureStatus failureStatus, String message) {
        super(failureStatus.getMessage() + '\n' + message);
        this.failureStatus = failureStatus;
    }

    public BaseException(FailureStatus failureStatus) {
        super(failureStatus.getMessage());
        this.failureStatus = failureStatus;
    }
}
