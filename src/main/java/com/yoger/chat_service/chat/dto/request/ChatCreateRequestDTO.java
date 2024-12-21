package com.yoger.chat_service.chat.dto.request;

import java.util.List;

public record ChatCreateRequestDTO(
        String title,
        String thumbnailUrl,
        List<Long> userIds
) {
}
