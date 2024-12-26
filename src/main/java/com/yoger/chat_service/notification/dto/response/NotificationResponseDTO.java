package com.yoger.chat_service.notification.dto.response;


import com.yoger.chat_service.notification.entity.NotificationEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationResponseDTO(@NotNull Long id, @NotNull Long userId, @NotBlank String content) {

    public static NotificationResponseDTO from(NotificationEntity notification) {
        return new NotificationResponseDTO(notification.getId(), notification.getUserId(), notification.getContent());
    }
}
