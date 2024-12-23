package com.yoger.chat_service.message.mapper;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;

public class MessageEntityMapper {

    public static MessageEntity toMessageEntity(ChatMessageRequestDTO chatMessageRequestDTO, ChatEntity chatEntity) {
        return new MessageEntity(
                chatMessageRequestDTO.chatId(),
                chatMessageRequestDTO.message(),
                chatEntity,
                chatMessageRequestDTO.senderId(),
                false
        );
    }
}
