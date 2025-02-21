package com.yoger.chat_service.websocket.service;

import com.yoger.chat_service.websocket.repository.InMemoryStompSessionStore;
import com.yoger.chat_service.websocket.repository.RedisStompSessionStore;
import com.yoger.chat_service.websocket.session.value.StompRedisSession;
import com.yoger.chat_service.websocket.session.value.StompUserSession;
import com.yoger.chat_service.websocket.session.key.ChatSessionKey;
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
public class ChatSessionService {

    private final InMemoryStompSessionStore inMemoryStompSessionStore;
    private final RedisStompSessionStore redisStompSessionStore;

    private final static String CHAT_SESSION = "CHAT_SESSION";

    //Subscribe
    public void subscribe(ChatSessionKey chatSessionKey, String sessionId, String subId) {
        redisStompSessionStore.store(CHAT_SESSION, chatSessionKey.getSessionKey(), sessionId, subId);
        inMemoryStompSessionStore.subscribe(sessionId, subId);
    }

    public void unSubscribe(String sessionId, String subId) {
        redisStompSessionStore.deleteBySessionIdAndSubId(CHAT_SESSION, sessionId, subId);
        inMemoryStompSessionStore.unSubscribe(sessionId, subId);
    }

    public void deleteAllBySessionIdRedis(String sessionId) {
        redisStompSessionStore.deleteAllBySessionId(CHAT_SESSION, sessionId);
    }

    public Map<String, List<StompUserSession>> findConnectedSessions(List<Long> receiverIds, String chatId) {
        Map<Object, Object> sessionMap = redisStompSessionStore.findSessionMapByName(CHAT_SESSION);
        Map<String, List<StompUserSession>> msgMap = new HashMap<>();

        receiverIds.forEach(id -> {
            StompRedisSession redisSession = (StompRedisSession) sessionMap.get(
                    new ChatSessionKey(chatId, id).getSessionKey());

            if (redisSession != null) {
                String serverUrl = redisSession.serverUrl();
                String sessionId = redisSession.sessionId();
                String subId = redisSession.subId();
                msgMap.computeIfAbsent(serverUrl, k -> new ArrayList<>())
                        .add(new StompUserSession(id, sessionId, subId));
            }
        });

        return msgMap;
    }

    public List<Long> findUnconnectedUserIds(List<Long> userIds, String chatId) {
        Map<Object, Object> sessionMap = redisStompSessionStore.findSessionMapByName(CHAT_SESSION);
        List<Long> nonConnectedUserIds = new ArrayList<>();

        userIds.forEach(id -> {
            StompRedisSession serverSession = (StompRedisSession) sessionMap.get(
                    new ChatSessionKey(chatId, id).getSessionKey());

            if (serverSession == null) {
                nonConnectedUserIds.add(id);
            }
        });

        return nonConnectedUserIds;
    }

    public boolean isInternallyConnected(String sessionId, String subId) {
        return inMemoryStompSessionStore.isSubscribed(sessionId, subId);
    }

    public void deleteAll() {
        inMemoryStompSessionStore.deleteAll();
        redisStompSessionStore.deleteAll(CHAT_SESSION);
    }
}

