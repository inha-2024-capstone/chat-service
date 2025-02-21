package com.yoger.chat_service.websocket.listener;

import com.yoger.chat_service.common.constant.SelfServerUrl;
import com.yoger.chat_service.websocket.service.ChatSessionService;
import com.yoger.chat_service.websocket.repository.InMemoryStompSessionStore;
import com.yoger.chat_service.websocket.service.PushSessionService;
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

    private final SelfServerUrl selfServerUrl;
    private final InMemoryStompSessionStore inMemoryStompSessionStore;
    private final ChatSessionService chatSessionService;
    private final PushSessionService pushSessionService;

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
        log.info("[STOMP SESSION CONNECT] key={}, serverId={}, sessionId={}", userId, selfServerUrl.getServerUrl(), sessionId);
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        chatSessionService.deleteAllBySessionIdRedis(sessionId);
        pushSessionService.deleteAllBySessionRedis(sessionId);
        inMemoryStompSessionStore.delete(sessionId);

        log.info("[STOMP SESSION DISCONNECT] sessionId={}", sessionId);
    }
}
