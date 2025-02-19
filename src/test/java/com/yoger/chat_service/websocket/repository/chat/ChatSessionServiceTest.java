package com.yoger.chat_service.websocket.repository.chat;

import static org.assertj.core.api.Assertions.*;

import com.yoger.chat_service.websocket.repository.ChatSessionService;
import com.yoger.chat_service.websocket.repository.InMemoryStompSessionStore;
import com.yoger.chat_service.websocket.session.key.ChatSessionKey;
import com.yoger.chat_service.websocket.session.value.StompUserSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatSessionServiceTest {

    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private InMemoryStompSessionStore inMemoryStompSessionStore;

    @Value("${spring.kafka.SERVER-IP}")
    private String serverUrl;

    @AfterEach
    void afterTest() {
        chatSessionService.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("sessionStoreArgs")
    @DisplayName("세션 정보 저장 및 조회 테스트")
    void 세션_저장_및_조회_테스트(List<ChatSessionKey> keys, List<String> sessionIds, String subId, List<Long> userIds,
                        List<Long> connected, List<Long> unConnected) {
        //given
        //when
        IntStream.range(0, 3).forEach(i -> {
            inMemoryStompSessionStore.store(sessionIds.get(i), keys.get(i).getUserId());
            chatSessionService.subscribe(keys.get(i), sessionIds.get(i), subId);
        });
        Map<String, List<StompUserSession>> connectedSessions = chatSessionService.findConnectedSessions(userIds, "1");
        List<Long> unconnectedUserIds = chatSessionService.findUnconnectedUserIds(userIds, "1");
        //then
        connectedSessions.forEach((key, value) -> {
            assertThat(key).isEqualTo(serverUrl);
            List<Long> actualIds = value.stream().map(StompUserSession::userId).toList();
            assertThat(actualIds).containsAll(connected);
        });
        assertThat(unconnectedUserIds).containsAll(unConnected);
    }

    private static Stream<Arguments> sessionStoreArgs() {
        List<String> sessionIds = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> {
            sessionIds.add(UUID.randomUUID().toString());
        });

        return Stream.of(
                Arguments.of(
                        List.of(new ChatSessionKey(1L, 1L),
                                new ChatSessionKey(1L, 2L),
                                new ChatSessionKey(1L, 3L)),
                        sessionIds,
                        "TEST SUB ID",
                        List.of(1L, 2L, 3L),
                        List.of(1L, 2L, 3L),
                        List.of()
                ),
                Arguments.of(
                        List.of(new ChatSessionKey(1L, 2L),
                                new ChatSessionKey(1L, 10L),
                                new ChatSessionKey(1L, 15L)),
                        sessionIds,
                        "TEST SUB ID",
                        List.of(2L, 10L, 100L),
                        List.of(2L, 10L),
                        List.of(100L)
                ),
                Arguments.of(
                        List.of(new ChatSessionKey(1L, 10L),
                                new ChatSessionKey(1L, 15L),
                                new ChatSessionKey(1L, 20L)),
                        sessionIds,
                        "TEST SUB ID",
                        List.of(10L, 150L, 20L),
                        List.of(10L, 20L),
                        List.of(150L)
                )
        );
    }

    @Test
    @DisplayName("세션 정보 삭제 테스트")
    void 세션_정보_삭제_테스트() {
        //given
        Long userId = 1L;
        Long chatId = 1L;
        ChatSessionKey chatSessionKey = new ChatSessionKey(chatId, userId);
        String sessionId = UUID.randomUUID().toString();
        String subId = "TEST SUB ID";

        StompUserSession expected = new StompUserSession(userId, sessionId, subId);
        //when
        inMemoryStompSessionStore.store(sessionId, String.valueOf(userId));
        chatSessionService.subscribe(chatSessionKey, sessionId, subId);
        Map<String, List<StompUserSession>> connectedSessionsBefore = chatSessionService.findConnectedSessions(List.of(userId),
                String.valueOf(chatId));

        chatSessionService.unSubscribe(sessionId, subId);

        Map<String, List<StompUserSession>> connectedSessionsAfter = chatSessionService.findConnectedSessions(List.of(userId),
                String.valueOf(chatId));

        //then
        assertThat(connectedSessionsBefore.size()).isEqualTo(1);
        assertThat(connectedSessionsBefore.get(serverUrl).size()).isEqualTo(1);
        assertThat(connectedSessionsBefore.get(serverUrl).getFirst()).isEqualTo(expected);
        assertThat(connectedSessionsAfter.size()).isEqualTo(0);
    }
}