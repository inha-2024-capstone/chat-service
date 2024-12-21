package com.yoger.chat_service.userchat.dto.request;

import com.yoger.chat_service.chat.domain.ChatEntity;
import java.util.List;

public record UserChatCreateRequestDTO(
        List<Long> userIds,
        ChatEntity chatEntity
) {
}
