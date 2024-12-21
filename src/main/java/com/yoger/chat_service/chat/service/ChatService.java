package com.yoger.chat_service.chat.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.chat.mapper.ChatEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatPersistService chatPersistService;

    @Transactional(readOnly = false)
    public void createChat(ChatCreateRequestDTO chatCreateRequest) {
        ChatEntity chatEntity = ChatEntityMapper.toChatEntity(chatCreateRequest);

        chatPersistService.save(chatEntity);
    }
}
