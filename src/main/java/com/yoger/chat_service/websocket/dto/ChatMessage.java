package com.yoger.chat_service.websocket.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import java.time.LocalDateTime;
import java.util.List;

public record ChatMessage(
        Long chatId,
        List<Long> receiverIds,
        String message,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
