package com.yoger.chat_service.userchat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.service.ChatPersistService;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import com.yoger.chat_service.message.service.MessagePersistService;
import com.yoger.chat_service.userInfo.domain.Role;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.service.UserInfoPersistService;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import com.yoger.chat_service.userchat.dto.response.UserChatListResponseDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserChatOrderByTest {

    @Autowired
    private UserChatService userChatService;

    @Autowired
    private MessagePersistService messagePersistService;

    @Autowired
    private ChatPersistService chatPersistService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    private Long chatId;

    @BeforeEach
    void createChat() {
        UserInfoEntity userInfo1 = new UserInfoEntity(1L, "test1", "test1@test.com", "https://test.com",
                Role.USER);
        UserInfoEntity userInfo2 = new UserInfoEntity(2L, "test2", "test2@test.com", "https://test.com",
                Role.USER);
        userInfoPersistService.save(userInfo1);
        userInfoPersistService.save(userInfo2);

        ChatEntity chatEntity = new ChatEntity(
                null,
                List.of(1L, 2L),
                null
        );

        List<UserChatCreateRequestDTO> requestDTOs = List.of(
                new UserChatCreateRequestDTO(
                        chatEntity,
                        1L,
                        2L,
                        "testName1"
                ),
                new UserChatCreateRequestDTO(
                        chatEntity,
                        2L,
                        1L,
                        "testName2"
                )
        );
        userChatService.createUserChats(requestDTOs);
        this.chatId = chatPersistService.save(chatEntity).getId();
    }
    @ParameterizedTest
    @MethodSource("userChatOrderArgs")
    @DisplayName("유저 챗에 마지막으로 도착한 메시지와 시간 확인")
    void 마지막_메시지_시간_테스트(List<Long> userIds, Integer cnt, String expectedMsg, LocalDateTime expectedTime) {
        //given
        List<ChatMessageEvent> msgEvents = createMsgEvents(cnt, userIds, expectedTime);
        //when
        msgEvents.forEach(messagePersistService::save);
        List<UserChatListResponseDTO> allUserChat1 = userChatService.getAllUserChat(userIds.getFirst());
        List<UserChatListResponseDTO> allUserChat2 = userChatService.getAllUserChat(userIds.getLast());
        //then
        assertThat(allUserChat1.getFirst().lastMessage()).usingDefaultComparator().isEqualTo(expectedMsg);
        assertThat(allUserChat2.getFirst().lastMessage()).usingDefaultComparator().isEqualTo(expectedMsg);
    }

    List<ChatMessageEvent> createMsgEvents(Integer cnt, List<Long> userIds, LocalDateTime time) {
        List<ChatMessageEvent> events = new ArrayList<>();

        for (int i = cnt - 1; i >= 0; i--) {
            events.add(new ChatMessageEvent(
                    chatId,
                    userIds.getFirst(),
                    UUID.randomUUID().toString(),
                    List.of(userIds.getFirst(), userIds.getLast()),
                    "test message" + i,
                    time.minusSeconds(i * 10L)
            ));
        }
        return events;
    }

    private Stream<Arguments> userChatOrderArgs() {
        return Stream.of(
                Arguments.of(
                        List.of(1L, 2L),
                        4,
                        "test message0",
                        LocalDateTime.now()
                )
        );
    }
}