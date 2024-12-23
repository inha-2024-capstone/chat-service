package com.yoger.chat_service.message.dto.response;

import com.yoger.chat_service.message.domain.MessageEntity;
import java.time.LocalDateTime;

public record ChatMessageResponseDTO(
        Long chatId,
        Long senderId,
        String message,
        LocalDateTime time
) {
    public static ChatMessageResponseDTO from(MessageEntity messageEntity) {
        return new ChatMessageResponseDTO(
                messageEntity.getChatEntity().getId(),
                messageEntity.getSenderId(),
                messageEntity.getMessage(),
                messageEntity.getCreateTime()
        );
    }
}
