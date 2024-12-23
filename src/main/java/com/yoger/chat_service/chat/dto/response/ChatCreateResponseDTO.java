package com.yoger.chat_service.chat.dto.response;

import com.yoger.chat_service.chat.domain.ChatEntity;
import java.util.List;

public record ChatCreateResponseDTO(
        Long chatId,
        String title,
        List<Long> userIds,
        String thumbnailUrl
) {
    public static ChatCreateResponseDTO from(ChatEntity chatEntity, List<Long> userIds) {
        return new ChatCreateResponseDTO(
                chatEntity.getId(),
                chatEntity.getTitle(),
                userIds,
                chatEntity.getThumbnailUrl()
        );
    }
}
