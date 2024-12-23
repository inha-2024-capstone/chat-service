package com.yoger.chat_service.chat.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.chat.dto.response.ChatCreateResponseDTO;
import com.yoger.chat_service.chat.mapper.ChatEntityMapper;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import com.yoger.chat_service.userchat.service.UserChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatPersistService chatPersistService;
    private final UserChatService userChatService;

    @Transactional(readOnly = false)
    public ChatCreateResponseDTO createChat(ChatCreateRequestDTO chatCreateRequestDTO) {
        ChatEntity chatEntity = ChatEntityMapper.toChatEntity(chatCreateRequestDTO);

        ChatEntity savedChatEntity = chatPersistService.save(chatEntity);

        List<UserChatCreateRequestDTO> userChatCreateRequests = chatCreateRequestDTO.userIds().stream()
                .map(id -> new UserChatCreateRequestDTO(id, savedChatEntity)).toList();

        userChatCreateRequests.forEach(userChatService::createUserChat);
        return ChatCreateResponseDTO.from(savedChatEntity, chatCreateRequestDTO.userIds());
    }
}
