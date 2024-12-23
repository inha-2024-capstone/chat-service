package com.yoger.chat_service.websocket.dto;

import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import java.time.LocalDateTime;
import java.util.List;

public record ChatMessage(
        Long chatId,
        List<Long> receiverIds,
        String message,
        LocalDateTime localDateTime,
        Boolean isRead) {
    public static ChatMessage from(ChatMessageRequestDTO chatMessageRequestDTO, MessageEntity messageEntity) {
        return new ChatMessage(
                chatMessageRequestDTO.chatId(),
                chatMessageRequestDTO.receiverIds(),
                chatMessageRequestDTO.message(),
                messageEntity.getCreateTime(),
                false
        );
    }

}
