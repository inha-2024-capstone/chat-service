package com.yoger.chat_service.websocket.session.key;

import com.yoger.chat_service.common.constant.StompConstant;
import lombok.Getter;

@Getter
public class PushSessionKey {
    private final String userId;

    public PushSessionKey(Long userId) {
        this.userId = String.valueOf(userId);
    }

    public String getSessionKey() {
        return StompConstant.PUSH_KEY_PREFIX + userId;
    }
}