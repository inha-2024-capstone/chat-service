package com.yoger.chat_service.websocket.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class StompSessionStore {
    private final Map<StompSessionKey, String> sessionStore = new ConcurrentHashMap<>();

    public void store(StompSessionKey stompSessionKey, String sessionId) {
        sessionStore.put(stompSessionKey, sessionId);
    }

    public void delete(StompSessionKey stompSessionKey) {
        sessionStore.remove(stompSessionKey);
    }

    public boolean isConnected(StompSessionKey stompSessionKey, String sessionId) {
        return sessionStore.get(stompSessionKey).equals(sessionId);
    }
}
