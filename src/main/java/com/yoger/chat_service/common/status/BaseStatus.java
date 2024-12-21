package com.yoger.chat_service.common.status;

import com.yoger.chat_service.common.response.BaseResponseBody;

public interface BaseStatus {
    <T> BaseResponseBody<T> getBaseResponseBody();

    <T> BaseResponseBody<T> getBaseResponseBody(T result);
}
