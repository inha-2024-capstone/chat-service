package com.yoger.chat_service.message.dto.request;

import com.yoger.chat_service.consumer.vo.SessionInfo;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import java.time.LocalDateTime;
import java.util.Map;

public record MessageBatchRequestDTO(
        Long chatId,
        Long senderId,
        Map<Long, SessionInfo> sessionMap,
        LocalDateTime sendAt,
        String message
) {
    public static MessageBatchRequestDTO from(ChatMessageEvent event, Map<Long, SessionInfo> sessionMap) {
        return new MessageBatchRequestDTO(
                event.chatId(),
                event.senderId(),
                sessionMap,
                event.createdAt(),
                event.chatMessage()
        );
    }
}
