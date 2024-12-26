package com.yoger.chat_service.notification.dto.response;


import com.yoger.chat_service.notification.entity.NotificationEntity;

import java.util.List;

public record NotificationResponseDTOs(List<NotificationResponseDTO> notificationResponseDTOs) {

    public static NotificationResponseDTOs from(List<NotificationEntity> notifications) {
        return new NotificationResponseDTOs(notifications.stream().map(NotificationResponseDTO::from).toList());
    }
}
