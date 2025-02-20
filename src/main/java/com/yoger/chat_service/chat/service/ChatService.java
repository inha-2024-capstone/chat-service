package com.yoger.chat_service.chat.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.chat.dto.response.ChatRoomResponseDTO;
import com.yoger.chat_service.chat.mapper.ChatEntityMapper;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.service.UserInfoQueryService;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import com.yoger.chat_service.userchat.service.UserChatService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatPersistService chatPersistService;
    private final UserInfoQueryService userInfoQueryService;
    private final UserChatService userChatService;

    @Transactional(readOnly = false)
    public ChatRoomResponseDTO createChat(ChatCreateRequestDTO requestDTO) {

        // 이미 생성된 채팅방이 있는지 확인
        Optional<ChatEntity> chatEntityOptional = userChatService.findChatEntityByUserIds(requestDTO);
        if (chatEntityOptional.isPresent()) {
            ChatEntity chatEntity = chatEntityOptional.get();
            List<UserInfoEntity> allByIds = userInfoQueryService.findAllByIds(chatEntity.getUserIds());
            return ChatRoomResponseDTO.from(chatEntity, allByIds);
        }

        return createNewChat(requestDTO);
    }

    public ChatRoomResponseDTO createNewChat(ChatCreateRequestDTO requestDTO) {
        // 채팅방 생성
        ChatEntity chatEntity = chatPersistService.save(ChatEntityMapper.toChatEntity(requestDTO));
        // 유저 정보 조회
        List<UserInfoEntity> userInfosById = userInfoQueryService.findAllByIds(chatEntity.getUserIds());
        // 유저 챗 생성
        UserChatCreateRequestDTO userRequestDTO1 = new UserChatCreateRequestDTO(chatEntity, userInfosById.getFirst()
                .getId(), userInfosById.getLast().getId(), userInfosById.getLast().getUserName());

        UserChatCreateRequestDTO userRequestDTO2 = new UserChatCreateRequestDTO(chatEntity, userInfosById.getLast()
                .getId(), userInfosById.getFirst().getId(), userInfosById.getFirst().getUserName());
        // 유저 챗 저장
        userChatService.createUserChats(List.of(userRequestDTO1, userRequestDTO2));

        return ChatRoomResponseDTO.from(chatEntity, userInfosById);
    }

    public ChatRoomResponseDTO getChat(Long chatId) {
        ChatEntity chatEntity = chatPersistService.findById(chatId);
        List<UserInfoEntity> userInfosById = userInfoQueryService.findAllByIds(chatEntity.getUserIds());
        return ChatRoomResponseDTO.from(chatEntity, userInfosById);
    }

    // 채팅방을 아예 삭제하는 경우
    public void deleteChatRoom(Long chatId) {
        ChatEntity chatEntity = chatPersistService.findById(chatId);
        userChatService.deleteUserChats(chatEntity.getUserIds());
        chatPersistService.deleteByChatId(chatId);
    }
}
