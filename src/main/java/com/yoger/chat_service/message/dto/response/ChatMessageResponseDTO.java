package com.yoger.chat_service.message.dto.response;

import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.dto.request.MessageBatchRequestDTO;
import java.time.LocalDateTime;

public record ChatMessageResponseDTO(
        Long chatId,
        Long senderId,
        String message,
        LocalDateTime chatSentTime
) {

    public static ChatMessageResponseDTO from(MessageBatchRequestDTO messageBatchRequestDTO) {
        return new ChatMessageResponseDTO(
                messageBatchRequestDTO.chatId(),
                messageBatchRequestDTO.senderId(),
                messageBatchRequestDTO.message(),
                messageBatchRequestDTO.sendAt()
        );
    }

    public static ChatMessageResponseDTO from(MessageEntity messageEntity) {
        return new ChatMessageResponseDTO(
                messageEntity.getChatEntity().getId(),
                messageEntity.getSenderId(),
                messageEntity.getMessage(),
                messageEntity.getCreateTime()
        );
    }
}
