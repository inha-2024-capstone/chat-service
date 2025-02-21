package com.yoger.chat_service.chat.dto.response;

import com.yoger.chat_service.userInfo.domain.UserInfoEntity;

public record ChatUserInfo(
        Long userId,
        String userName,
        String imageUrl,
        String email
) {
    public static ChatUserInfo from(UserInfoEntity userInfo) {
        return new ChatUserInfo(
                userInfo.getId(),
                userInfo.getUserName(),
                userInfo.getImageUrl(),
                userInfo.getEmail()
        );
    }
}
