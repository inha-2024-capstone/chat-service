package com.yoger.chat_service.userchat.service;

import static org.assertj.core.api.Assertions.*;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserChatServiceTest {

    @Autowired
    private UserChatService userChatService;

    @Autowired
    private UserChatPersistService userChatPersistService;


    @ParameterizedTest
    @MethodSource("userChatCreateArgs")
    @DisplayName("1:1 채팅방 생성 및 유저 채팅 생성")
    void 유저챗_생성_테스트(List<UserChatCreateRequestDTO> requestDTOs,
                    Long expectedUserId, Long expectedRcvId, List<String> expectedTitles) {
        //given
        //when
        userChatService.createUserChats(requestDTOs);
        //then

        // 첫 번째 유저 챗
        List<UserChatEntity> allByUserId1 = userChatPersistService.findAllByUserIdTest(expectedUserId);

        assertThat(allByUserId1.getFirst().getChatEntity().getUserIds()).isNotNull();
        assertThat(allByUserId1.getFirst().getUserId()).usingRecursiveComparison().isEqualTo(expectedUserId);
        assertThat(allByUserId1.getFirst().getReceiverId()).usingRecursiveComparison().isEqualTo(expectedRcvId);
        assertThat(allByUserId1.getFirst().getTitle()).isEqualTo(expectedTitles.getFirst());

        // 두 번째 유저 챗
        List<UserChatEntity> allByUserId2 = userChatPersistService.findAllByUserIdTest(expectedRcvId);

        assertThat(allByUserId2.getFirst().getChatEntity().getUserIds()).isNotNull();
        assertThat(allByUserId2.getFirst().getUserId()).usingRecursiveComparison().isEqualTo(expectedRcvId);
        assertThat(allByUserId2.getFirst().getReceiverId()).usingRecursiveComparison().isEqualTo(expectedUserId);
        assertThat(allByUserId2.getFirst().getTitle()).isEqualTo(expectedTitles.getLast());

    }

    private static Stream<Arguments> userChatCreateArgs() {
        ChatEntity chatEntity = new ChatEntity(
                null,
                List.of(1L, 2L),
                null
        );
        return Stream.of(
                Arguments.of(
                        List.of(
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
                        ),
                        1L,
                        2L,
                        List.of("testName1", "testName2")
                )
        );
    }
}