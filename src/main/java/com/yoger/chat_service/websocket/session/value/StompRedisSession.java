package com.yoger.chat_service.websocket.session.value;

public record StompRedisSession(
        String serverUrl,
        String sessionId,
        String subId
) {
}
