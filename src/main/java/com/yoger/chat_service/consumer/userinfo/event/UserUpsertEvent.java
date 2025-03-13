package com.yoger.chat_service.consumer.userinfo.event;

import com.yoger.chat_service.userInfo.domain.Role;

public record UserUpsertEvent(
        Long id,
        String username,
        String email,
        String imageUrl,
        Role role,
        String eventId
) {
}
