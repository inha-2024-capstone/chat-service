package com.yoger.chat_service.chat.mapper;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;

public class ChatEntityMapper {

    public static ChatEntity toChatEntity(ChatCreateRequestDTO chatCreateRequestDTO) {
        return new ChatEntity(
                null,
                chatCreateRequestDTO.title(),
                null,
                null
        );
    }
}
