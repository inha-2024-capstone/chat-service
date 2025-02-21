package com.yoger.chat_service.chat.mapper;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import java.util.List;

public class ChatEntityMapper {

    public static ChatEntity toChatEntity(ChatCreateRequestDTO chatCreateRequestDTO) {
        return new ChatEntity(
                null,
                List.of(chatCreateRequestDTO.senderId(), chatCreateRequestDTO.receiverId()),
                null
        );
    }
}