package com.yoger.chat_service.chat.service;

import static org.assertj.core.api.Assertions.*;

import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.chat.dto.response.ChatRoomResponseDTO;
import com.yoger.chat_service.chat.dto.response.ChatUserInfo;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import com.yoger.chat_service.message.service.MessagePersistService;
import com.yoger.chat_service.userInfo.domain.Role;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.service.UserInfoPersistService;
import com.yoger.chat_service.userchat.dto.response.UserChatListResponseDTO;
import com.yoger.chat_service.userchat.service.UserChatService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChatServiceTest {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessagePersistService messagePersistService;
    @Autowired
    private UserChatService userChatService;

    @Autowired
    private UserInfoPersistService userInfoPersistService;

    private List<ChatUserInfo> expectedChatUserInfo;

    @BeforeEach
    void createUserInfo() {
        UserInfoEntity userInfo1 = new UserInfoEntity(1L, "test1", "test1@test.com", "https://test.com",
                Role.USER);
        UserInfoEntity userInfo2 = new UserInfoEntity(2L, "test2", "test2@test.com", "https://test.com",
                Role.USER);
        userInfoPersistService.save(userInfo1);
        userInfoPersistService.save(userInfo2);

        expectedChatUserInfo = List.of(
                new ChatUserInfo(
                        1L, "test1", "https://test.com", "test1@test.com"),
                new ChatUserInfo(
                        2L, "test2", "https://test.com", "test2@test.com")
        );
    }


    @ParameterizedTest
    @MethodSource("createChatTestArgs")
    @DisplayName("채팅방, 유저 챗을 생성하는 테스트")
    void 채팅방_유저챗_생성_테스트(ChatCreateRequestDTO requestDTO, String expectedMsg) {
        //given
        //when
        ChatRoomResponseDTO responseDTO = chatService.createChat(requestDTO);
        ChatMessageEvent chatMessageEvent = createChatMessageEvent(requestDTO, responseDTO, expectedMsg);
        messagePersistService.save(chatMessageEvent);

        //then
        List<UserChatListResponseDTO> allUserChat1 = userChatService.getAllUserChat(requestDTO.senderId());
        List<UserChatListResponseDTO> allUserChat2 = userChatService.getAllUserChat(requestDTO.receiverId());

        assertThat(allUserChat1.size()).isEqualTo(1);
        assertThat(allUserChat1.getFirst().imageUrl()).isEqualTo(expectedChatUserInfo.getFirst().imageUrl());
        assertThat(allUserChat1.getFirst().title()).isEqualTo(expectedChatUserInfo.getLast().userName());
        assertThat(allUserChat1.getFirst().lastMessage()).isEqualTo(expectedMsg);

        assertThat(allUserChat2.size()).isEqualTo(1);
        assertThat(allUserChat2.getFirst().imageUrl()).isEqualTo(expectedChatUserInfo.getLast().imageUrl());
        assertThat(allUserChat2.getFirst().title()).isEqualTo(expectedChatUserInfo.getFirst().userName());
        assertThat(allUserChat2.getFirst().lastMessage()).isEqualTo(expectedMsg);

        assertThat(responseDTO.chatUserInfos()).containsExactlyElementsOf(expectedChatUserInfo);
    }

    private ChatMessageEvent createChatMessageEvent(ChatCreateRequestDTO requestDTO, ChatRoomResponseDTO responseDTO,
                                                    String expectedMessage) {
        return new ChatMessageEvent(
                responseDTO.chatId(),
                requestDTO.senderId(),
                UUID.randomUUID().toString(),
                List.of(requestDTO.senderId(), requestDTO.receiverId()),
                expectedMessage,
                LocalDateTime.now()
        );
    }

    @ParameterizedTest
    @MethodSource("createChatTestArgs")
    @DisplayName("이미 생성된 채팅방, 유저 챗 가져오는 테스트")
    void 이미_존재하는_채팅방_가져오기(ChatCreateRequestDTO requestDTO) {
        //given
        //when
        ChatRoomResponseDTO expectedChatRoom = chatService.createChat(requestDTO);
        ChatRoomResponseDTO actualChatRoom = chatService.createChat(requestDTO);

        //then
        assertThat(actualChatRoom).usingRecursiveComparison().isEqualTo(expectedChatRoom);
    }

    private static Stream<Arguments> createChatTestArgs() {
        return Stream.of(
                Arguments.of(
                        new ChatCreateRequestDTO(
                                1L,
                                2L
                        ),
                        "test message1"
                )
        );
    }
}