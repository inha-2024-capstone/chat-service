package com.yoger.chat_service.chat.dto.response;

import java.util.List;

public record ChatCreateResponseDTO(
        String id,
        String title,
        List<Long> userIds,
        String thumbnailUrl
) {
}
