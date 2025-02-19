package com.yoger.chat_service.message.event;

import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChatMessageEvent(
        Long chatId,
        Long senderId,
        String eventId,
        List<Long> receiverIds,
        String chatMessage,
        LocalDateTime createdAt) {

    public static ChatMessageEvent from(ChatMessageRequestDTO chatMessageRequestDTO) {
        return new ChatMessageEvent(
                chatMessageRequestDTO.chatId(),
                chatMessageRequestDTO.senderId(),
                UUID.randomUUID().toString(),
                chatMessageRequestDTO.receiverIds(),
                chatMessageRequestDTO.message(),
                LocalDateTime.now());
    }
}
