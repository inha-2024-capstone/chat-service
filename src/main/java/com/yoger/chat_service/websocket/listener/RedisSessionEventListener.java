package com.yoger.chat_service.websocket.listener;

import com.yoger.chat_service.websocket.config.ServerId;
import com.yoger.chat_service.websocket.session.StompSessionKey;
import com.yoger.chat_service.websocket.session.StompSessionStore;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class RedisSessionEventListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String serverId; // 현재 서버를 구분할 수 있는 ID
    private final StompSessionStore stompSessionStore;

    public RedisSessionEventListener(RedisTemplate<String, Object> redisTemplate, ServerId serverId,
                                     StompSessionStore stompSessionStore) {
        this.redisTemplate = redisTemplate;
        this.serverId = serverId.getServerId();
        this.stompSessionStore = stompSessionStore;
    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String chatId = headerAccessor.getFirstNativeHeader("Chat-Id");
        String userId = headerAccessor.getFirstNativeHeader("User-Id");

        String sessionId = headerAccessor.getSessionId();

        StompSessionKey stompSessionKey = new StompSessionKey(chatId, userId);
        storeStompSession(stompSessionKey, sessionId);
    }

    private void storeStompSession(StompSessionKey stompSessionKey, String sessionId) {
        Map<String, String> value = new HashMap<>();
        value.put("serverId", serverId);
        value.put("sessionId", sessionId);

        stompSessionStore.store(stompSessionKey, sessionId);

        redisTemplate.opsForHash().put("WS_SESSION", stompSessionKey.getSessionKey(), value);
        log.info("[CONNECT] key={}, serverId={}, sessionId={}", stompSessionKey.getSessionKey(), serverId, sessionId);
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        Map<Object, Object> allEntries = redisTemplate.opsForHash().entries("WS_SESSION");
        for (Map.Entry<Object, Object> entry : allEntries.entrySet()) {
            StompSessionKey key = new StompSessionKey((String) entry.getKey());
            Map<String, String> val = (Map<String, String>) entry.getValue();

            if (val.get("sessionId").equals(sessionId)) {
                stompSessionStore.delete(key);
                redisTemplate.opsForHash().delete("WS_SESSION", key.getSessionKey());
                log.info("[DISCONNECT] key={}, sessionId={}", key, sessionId);
                break;
            }
        }
    }
}