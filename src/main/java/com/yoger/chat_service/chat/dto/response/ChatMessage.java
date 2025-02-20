package com.yoger.chat_service.chat.dto.response;

import com.yoger.chat_service.message.domain.MessageEntity;
import java.time.LocalDateTime;

public record ChatMessage(
        Long senderId,
        String message,
        LocalDateTime createdAt
) {
    public static ChatMessage from(MessageEntity messageEntity) {
        return new ChatMessage(
                messageEntity.getSenderId(),
                messageEntity.getMessage(),
                messageEntity.getCreateTime()
        );
    }
}
