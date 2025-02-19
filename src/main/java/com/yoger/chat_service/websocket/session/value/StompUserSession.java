package com.yoger.chat_service.websocket.session.value;

public record StompUserSession(
        Long userId,
        String sessionId,
        String subId
) {
}
