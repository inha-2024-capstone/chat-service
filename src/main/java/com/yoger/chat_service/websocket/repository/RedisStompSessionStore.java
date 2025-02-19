package com.yoger.chat_service.websocket.repository;

import com.yoger.chat_service.common.constant.ServerUrl;
import com.yoger.chat_service.websocket.session.value.StompRedisSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisStompSessionStore {
    private final RedisTemplate<String, StompRedisSession> sessionTemplate;
    private final ServerUrl serverUrl;

    public void store(String sessionName, String sessionKey, String sessionId, String subId) {
        sessionTemplate.opsForHash()
                .put(sessionName, sessionKey, new StompRedisSession(serverUrl.getServerUrl(), sessionId, subId));
    }

    public StompRedisSession findBySessionKey(String sessionName, String sessionKey) {
        return (StompRedisSession) sessionTemplate.opsForHash().get(sessionName, sessionKey);
    }

    public Map<Object, Object> findSessionMapByName(String sessionName) {
        return sessionTemplate.opsForHash().entries(sessionName);
    }

    public void deleteBySessionKey(String sessionName, String sessionKey) {
        sessionTemplate.opsForHash().delete(sessionName, sessionKey);
    }

    public void deleteAllBySessionId(String sessionName, String sessionId) {
        Map<Object, Object> sessionMap = sessionTemplate.opsForHash().entries(sessionName);

        for (Map.Entry<Object, Object> entry : sessionMap.entrySet()) {

            StompRedisSession value = (StompRedisSession) entry.getValue();
            if (value.sessionId().equals(sessionId)) {
                sessionTemplate.opsForHash().delete(sessionName, entry.getKey());
            }
        }
    }

    public void deleteBySessionIdAndSubId(String sessionName, String sessionId, String subId) {
        Map<Object, Object> sessionMap = findSessionMapByName(sessionName);

        for (Map.Entry<Object, Object> entry : sessionMap.entrySet()) {

            StompRedisSession value = (StompRedisSession) entry.getValue();
            if (value.sessionId().equals(sessionId) && value.subId().equals(subId)) {
                sessionTemplate.opsForHash().delete(sessionName, entry.getKey());
                return;
            }
        }
    }

    public void deleteAll(String sessionName) {
        sessionTemplate.delete(sessionName);
    }
}
