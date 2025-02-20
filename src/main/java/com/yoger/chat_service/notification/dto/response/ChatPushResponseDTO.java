package com.yoger.chat_service.notification.dto.response;

import com.yoger.chat_service.notification.domain.PushEntity;
import com.yoger.chat_service.notification.domain.PushType;
import java.time.LocalDateTime;

public record ChatPushResponseDTO(
        Long pushId,
        Long chatId,
        String message,
        LocalDateTime createdAt,
        PushType pushType,
        Boolean isRead
) {
    public static ChatPushResponseDTO from(PushEntity pushEntity) {
        return new ChatPushResponseDTO(
                pushEntity.getId(),
                pushEntity.getEventId(),
                pushEntity.getMessage(),
                pushEntity.getCreateTime(),
                pushEntity.getType(),
                pushEntity.getIsRead()
        );
    }
}
