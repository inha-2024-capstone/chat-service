package com.yoger.chat_service.notification.dto.request;

import com.yoger.chat_service.consumer.vo.SessionInfo;
import com.yoger.chat_service.message.dto.request.MessageBatchRequestDTO;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import java.time.LocalDateTime;
import java.util.Map;

public record ChatPushBatchRequestDTO(
        Long chatId,
        Long senderId,
        String chatMessage,
        LocalDateTime pushedAt,
        Map<Long, SessionInfo> sessionMap
) {
    public static ChatPushBatchRequestDTO from(ChatMessageEvent chatMessageEvent, Map<Long, SessionInfo> sessionMap,
                                               String pushMsg) {
        return new ChatPushBatchRequestDTO(
                chatMessageEvent.chatId(),
                chatMessageEvent.senderId(),
                pushMsg,
                chatMessageEvent.createdAt(),
                sessionMap
        );
    }

    public static ChatPushBatchRequestDTO from(MessageBatchRequestDTO requestDTO, Map<Long, SessionInfo> sessionMap,
                                               String pushMsg) {
        return new ChatPushBatchRequestDTO(
                requestDTO.chatId(),
                requestDTO.senderId(),
                pushMsg,
                requestDTO.sendAt(),
                sessionMap
        );
    }
}
