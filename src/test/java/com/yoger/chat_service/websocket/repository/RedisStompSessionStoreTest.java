package com.yoger.chat_service.websocket.repository;

import static org.assertj.core.api.Assertions.*;

import com.yoger.chat_service.common.UsingRedisTest;
import com.yoger.chat_service.common.constant.SelfServerUrl;
import com.yoger.chat_service.websocket.session.value.StompRedisSession;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisStompSessionStoreTest extends UsingRedisTest {

    @Autowired
    private RedisStompSessionStore redisStompSessionStore;

    @Autowired
    private SelfServerUrl selfServerUrl;
    private static final String sessionName = "TEST";

    @AfterEach
    void afterTest() {
        redisStompSessionStore.deleteAll(sessionName);
    }

    @Test
    @DisplayName("레디스에 세션 정보 저장")
    void 세션_저장_테스트() {
        //given
        String sessionKey = "test:1";
        String sessionId = "TEST_ID";
        String subId = "TEST SUB ID";

        StompRedisSession expectedSession = new StompRedisSession(selfServerUrl.getServerUrl(), sessionId, subId);
        //when
        redisStompSessionStore.store(sessionName, sessionKey, sessionId, subId);
        //then
        StompRedisSession actualSession = redisStompSessionStore.findBySessionKey(sessionName, sessionKey);
        assertThat(actualSession).usingRecursiveComparison().isEqualTo(expectedSession);
    }

    @Test
    @DisplayName("세션키를 통한 레디스에 세션 정보 삭제")
    void 세션_삭제_테스트1() {
        //given
        String sessionKey = "test:1";
        String sessionId = "TEST_ID";
        String subId = "TEST SUB ID";
        StompRedisSession session = new StompRedisSession(selfServerUrl.getServerUrl(), sessionId, subId);
        //when
        redisStompSessionStore.store(sessionName, sessionKey, sessionId, subId);
        //then
        StompRedisSession actualSession = redisStompSessionStore.findBySessionKey(sessionName, sessionKey);
        assertThat(actualSession).usingRecursiveComparison().isEqualTo(session);
        //after delete
        redisStompSessionStore.deleteBySessionKey(sessionName, sessionKey);
        StompRedisSession deletedSession = redisStompSessionStore.findBySessionKey(sessionName, sessionKey);
        assertThat(deletedSession).usingRecursiveComparison().isEqualTo(null);
    }

    @Test
    @DisplayName("세션ID를 통한 세션 정보 삭제")
    void 세션_삭제_테스트2() {
        //given
        String sessionKey = "test:1";
        String sessionId = "TEST_ID";
        String subId = "TEST SUB ID";
        StompRedisSession session = new StompRedisSession(selfServerUrl.getServerUrl(), sessionId, subId);
        //when
        redisStompSessionStore.store(sessionName, sessionKey, sessionId, subId);
        //then
        StompRedisSession actualSession = redisStompSessionStore.findBySessionKey(sessionName, sessionKey);
        assertThat(actualSession).usingRecursiveComparison().isEqualTo(session);
        //after deletion
        redisStompSessionStore.deleteAllBySessionId(sessionName, session.sessionId());
        StompRedisSession deleteSession = redisStompSessionStore.findBySessionKey(sessionName, sessionKey);
        assertThat(deleteSession).usingRecursiveComparison().isEqualTo(null);
    }

    @Test
    @DisplayName("전체 세션 조회 테스트")
    void 전체_세션_조회_테스트() {
        //given
        String sessionKey1 = "test:1";
        String sessionKey2 = "test:2";
        String sessionId = "TEST_ID";
        String subId = "TEST SUB ID";
        StompRedisSession session = new StompRedisSession(selfServerUrl.getServerUrl(), sessionId, subId);
        //when
        redisStompSessionStore.store(sessionName, sessionKey1, sessionId, subId);
        redisStompSessionStore.store(sessionName, sessionKey2, sessionId, subId);
        Map<Object, Object> sessionMap = redisStompSessionStore.findSessionMapByName(sessionName);
        //then
        assertThat(sessionMap.get(sessionKey1)).usingRecursiveComparison().isEqualTo(session);
        assertThat(sessionMap.get(sessionKey2)).usingRecursiveComparison().isEqualTo(session);
    }
}