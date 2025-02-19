package com.yoger.chat_service.websocket.session.value;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

@Getter
public class StompInMemorySession {

    private final String sessionId;
    private final List<String> subIds = new CopyOnWriteArrayList<>();
    private final Long userId;

    public StompInMemorySession(String sessionId, Long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public StompInMemorySession(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = Long.valueOf(userId);
    }

    public void unSubscribe(String subId) {
        if (!subIds.contains(subId)) {
            throw new RuntimeException("존재하지 않는 Sub ID 입니다. sub id: " + subId);
        }
        subIds.remove(subId);
    }

    public void subscribe(String subId) {
        if (subIds.contains(subId)) {
            throw new RuntimeException("이미 존재하는 Sub ID 입니다. sub id: " + subId);
        }
        subIds.add(subId);
    }
}
