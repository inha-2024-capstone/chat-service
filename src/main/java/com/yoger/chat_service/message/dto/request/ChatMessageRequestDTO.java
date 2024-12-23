package com.yoger.chat_service.message.dto.request;

import java.util.List;

public record ChatMessageRequestDTO(
        Long chatId,
        Long senderId,
        List<Long> receiverIds,
        String message
) {
}
