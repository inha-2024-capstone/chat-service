package com.yoger.chat_service.notification.dto.response;

import java.util.List;

public record ChatPushesResponseDTO(
        List<ChatPushResponseDTO> chatPushResponseDTOs) {

    public static ChatPushesResponseDTO from(List<ChatPushResponseDTO> chatPushResponseDTOs){
        return new ChatPushesResponseDTO(chatPushResponseDTOs);
    }
}
