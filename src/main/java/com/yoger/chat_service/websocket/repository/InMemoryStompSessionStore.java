package com.yoger.chat_service.websocket.repository;

import com.yoger.chat_service.websocket.session.value.StompInMemorySession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InMemoryStompSessionStore {

    private final Map<String, StompInMemorySession> sessionStore = new ConcurrentHashMap<>();

    public void store(String sessionId, String userId) {
        sessionStore.put(sessionId, new StompInMemorySession(sessionId, userId));
    }

    public void subscribe(String sessionId, String subId) {
        StompInMemorySession stompInMemorySession = sessionStore.get(sessionId);
        stompInMemorySession.subscribe(subId);
        sessionStore.replace(sessionId, stompInMemorySession);
    }

    public void unSubscribe(String sessionId, String subId) {
        StompInMemorySession stompInMemorySession = sessionStore.get(sessionId);
        stompInMemorySession.unSubscribe(subId);
        sessionStore.replace(sessionId, stompInMemorySession);
    }

    public void delete(String sessionId) {
        sessionStore.remove(sessionId);
    }

    public void deleteAll() {
        sessionStore.clear();
    }

    public Long findUserIdBySessionId(String sessionId) {
        StompInMemorySession stompInMemorySession = sessionStore.get(sessionId);
        if (stompInMemorySession == null) {
            throw new RuntimeException("해당 세션은 존재하지 않습니다. Session Id: " + sessionId);
        }
        return stompInMemorySession.getUserId();
    }

    public boolean isSubscribed(String sessionId, String subId) {
        StompInMemorySession stompInMemorySession = sessionStore.get(sessionId);
        if (stompInMemorySession == null) {
            return false;
        }
        return stompInMemorySession.getSubIds().contains(subId);
    }
}
