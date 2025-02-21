package com.yoger.chat_service.websocket.service;

import com.yoger.chat_service.websocket.repository.InMemoryStompSessionStore;
import com.yoger.chat_service.websocket.repository.RedisStompSessionStore;
import com.yoger.chat_service.websocket.session.value.StompRedisSession;
import com.yoger.chat_service.websocket.session.value.StompUserSession;
import com.yoger.chat_service.websocket.session.key.PushSessionKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushSessionService {
    private final InMemoryStompSessionStore inMemoryStompSessionStore;
    private final RedisStompSessionStore redisStompSessionStore;

    private final static String PUSH_SESSION = "PUSH_SESSION";

    public void subscribe(PushSessionKey pushSessionKey, String sessionId, String subId) {
        redisStompSessionStore.store(PUSH_SESSION, pushSessionKey.getUserId(), sessionId, subId);
        inMemoryStompSessionStore.subscribe(sessionId, subId);
    }

    public void unSubscribe(String sessionId, String subId) {
        redisStompSessionStore.deleteBySessionIdAndSubId(PUSH_SESSION, sessionId, subId);
        inMemoryStompSessionStore.unSubscribe(sessionId, subId);
    }

    public void deleteAllBySessionRedis(String sessionId) {
        redisStompSessionStore.deleteAllBySessionId(PUSH_SESSION, sessionId);
    }

    public Map<String, List<StompUserSession>> findConnectedSessions(List<Long> receiverIds) {
        Map<Object, Object> sessionMap = redisStompSessionStore.findSessionMapByName(PUSH_SESSION);
        Map<String, List<StompUserSession>> pushMap = new HashMap<>();

        receiverIds.forEach(id -> {

            StompRedisSession serverSession = (StompRedisSession) sessionMap.get(new PushSessionKey(id).getUserId());

            if (serverSession != null) {
                String serverUrl = serverSession.serverUrl();
                String sessionId = serverSession.sessionId();
                String subId = serverSession.subId();
                log.info("[Push Session STORE] serverUrl={}, sessionId={}", serverUrl, sessionId);
                pushMap.computeIfAbsent(serverUrl, k -> new ArrayList<>())
                        .add(new StompUserSession(id, sessionId, subId));
            }
        });

        return pushMap;
    }
}
