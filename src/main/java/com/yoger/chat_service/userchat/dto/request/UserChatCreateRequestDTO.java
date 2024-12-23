package com.yoger.chat_service.userchat.dto.request;

import com.yoger.chat_service.chat.domain.ChatEntity;

public record UserChatCreateRequestDTO(
        Long id,
        ChatEntity chatEntity
) {
}
