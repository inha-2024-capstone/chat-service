package com.yoger.chat_service.websocket.listener;

import com.yoger.chat_service.websocket.config.ServerId;
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

    public RedisSessionEventListener(RedisTemplate<String, Object> redisTemplate, ServerId serverId) {
        this.redisTemplate = redisTemplate;
        // 예시로 랜덤 UUID 등 서버 식별자
        this.serverId = serverId.getServerId();
    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String chatId = headerAccessor.getFirstNativeHeader("chatId");
        String userId = headerAccessor.getFirstNativeHeader("userId");
        String sessionId = headerAccessor.getSessionId();

        if (chatId != null && userId != null) {
            String key = chatId + ":" + userId; // 예: "room1:user123"

            // 서버ID, 세션ID를 저장
            Map<String, String> value = new HashMap<>();
            value.put("serverId", serverId);
            value.put("sessionId", sessionId);

            // 해시 구조로 관리
            redisTemplate.opsForHash().put("WS_SESSION", key, value);

            log.info("[CONNECT] key={}, serverId={}, sessionId={}", key, serverId, sessionId);
        }
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // "WS_SESSION" 해시 전체를 뒤져 sessionId가 일치하는 항목을 찾음
        Map<Object, Object> allEntries = redisTemplate.opsForHash().entries("WS_SESSION");
        for (Map.Entry<Object, Object> entry : allEntries.entrySet()) {
            String key = (String) entry.getKey();
            Map<String, String> val = (Map<String, String>) entry.getValue();

            if (val.get("sessionId").equals(sessionId)) {
                redisTemplate.opsForHash().delete("WS_SESSION", key);
                log.info("[DISCONNECT] key={}, sessionId={}", key, sessionId);
                break;
            }
        }
    }
}