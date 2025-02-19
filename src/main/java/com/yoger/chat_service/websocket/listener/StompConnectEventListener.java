package com.yoger.chat_service.websocket.listener;

import com.yoger.chat_service.common.constant.ServerUrl;
import com.yoger.chat_service.websocket.repository.ChatSessionStore;
import com.yoger.chat_service.websocket.repository.InMemoryStompSessionStore;
import com.yoger.chat_service.websocket.repository.PushSessionStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompConnectEventListener {

    private final ServerUrl serverUrl;
    private final InMemoryStompSessionStore inMemoryStompSessionStore;
    private final ChatSessionStore chatSessionStore;
    private final PushSessionStore pushSessionStore;

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        //유저 ID를 Connect Header 에서 가져옴
        String userId = headerAccessor.getFirstNativeHeader("User-Id");

        String sessionId = headerAccessor.getSessionId();

        storeStompSession(userId, sessionId);
    }

    private void storeStompSession(String userId, String sessionId) {
        inMemoryStompSessionStore.store(sessionId, userId);
        log.info("[STOMP SESSION CONNECT] key={}, serverId={}, sessionId={}", userId, serverUrl.getServerUrl(), sessionId);
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        chatSessionStore.deleteAllBySessionIdRedis(sessionId);
        pushSessionStore.deleteAllBySessionRedis(sessionId);
        inMemoryStompSessionStore.delete(sessionId);

        log.info("[STOMP SESSION DISCONNECT] sessionId={}", sessionId);
    }
}
