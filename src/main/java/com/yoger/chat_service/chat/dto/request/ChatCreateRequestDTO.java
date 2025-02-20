package com.yoger.chat_service.chat.dto.request;

public record ChatCreateRequestDTO(
        Long senderId,
        Long receiverId
) {
}
