package com.yoger.chat_service.common.status.enums;

import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.BaseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuccessStatus implements BaseStatus {

    OK(HttpStatus.OK, "COMMON2000", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON2010", "생성되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public <T> BaseResponseBody<T> getBaseResponseBody(T result) {
        return new BaseResponseBody<>(true, this.code, this.message, result);
    }

    @Override
    public <T> BaseResponseBody<T> getBaseResponseBody() {
        return new BaseResponseBody<>(true, this.code, this.message, null);
    }
}
