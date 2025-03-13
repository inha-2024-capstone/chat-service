package com.yoger.chat_service.websocket.repository;

import static org.assertj.core.api.Assertions.*;

import com.yoger.chat_service.common.UsingRedisTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InMemoryStompSessionStoreTest extends UsingRedisTest {

    @Autowired
    private InMemoryStompSessionStore inMemoryStompSessionStore;

    @AfterEach
    void afterTest() {
        inMemoryStompSessionStore.deleteAll();
    }

    @Test
    @DisplayName("세션 정보 메모리 저장")
    void 세션정보_메모리_저장() {
        //given
        String sessionId = "TEST SESSION ID";
        Long userId = 2L;

        //when
        inMemoryStompSessionStore.store(sessionId, String.valueOf(userId));
        Long actualUserId = inMemoryStompSessionStore.findUserIdBySessionId(sessionId);

        //when
        assertThat(actualUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("세션 정보 삭제 테스트")
    void 세션_정보_삭제_테스트() {
        //given
        String sessionId = "TEST SESSION ID";
        Long userId = 2L;

        //when
        inMemoryStompSessionStore.store(sessionId, String.valueOf(userId));
        inMemoryStompSessionStore.delete(sessionId);

        //then
        assertThatThrownBy(() -> {
            inMemoryStompSessionStore.findUserIdBySessionId(sessionId);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("구독을 수행하는 테스트")
    void 구독_테스트() {
        //given
        String sessionId = "TEST SESSION ID";
        String subId = "TEST SUB ID";
        Long userId = 2L;

        //when
        inMemoryStompSessionStore.store(sessionId, String.valueOf(userId));
        inMemoryStompSessionStore.subscribe(sessionId, subId);
        boolean isSubscribed = inMemoryStompSessionStore.isSubscribed(sessionId, subId);

        //then
        assertThat(isSubscribed).isTrue();
    }

    @Test
    @DisplayName("구독 해제를 수행하는 테스트")
    void 구독_해제_테스트() {
        //given
        String sessionId = "TEST SESSION ID";
        String subId = "TEST SUB ID";
        Long userId = 2L;

        //when
        inMemoryStompSessionStore.store(sessionId, String.valueOf(userId));
        inMemoryStompSessionStore.subscribe(sessionId, subId);
        boolean isSubscribedAfterSubscription = inMemoryStompSessionStore.isSubscribed(sessionId, subId);
        inMemoryStompSessionStore.unSubscribe(sessionId, subId);
        boolean isSubscribedAfterUnSubscription = inMemoryStompSessionStore.isSubscribed(sessionId, subId);

        //then
        assertThat(isSubscribedAfterSubscription).isTrue();
        assertThat(isSubscribedAfterUnSubscription).isFalse();
    }
}