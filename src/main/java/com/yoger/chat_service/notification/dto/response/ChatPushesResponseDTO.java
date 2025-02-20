package com.yoger.chat_service.notification.dto.response;

import java.util.List;

public record ChatPushesResponseDTO(
        List<ChatPushResponseDTO> chatPushResponseDTOs
) {
}
