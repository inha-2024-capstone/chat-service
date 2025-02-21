package com.yoger.chat_service.common.status;

import com.yoger.chat_service.common.response.BaseResponseBody;
import org.springframework.http.ResponseEntity;

public interface BaseStatus {
    <T> ResponseEntity<BaseResponseBody<T>> getResponseBody(T result);

    <T> ResponseEntity<BaseResponseBody<T>> getResponseBody();
}
