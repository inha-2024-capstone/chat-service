package com.yoger.chat_service.userchat.dto.response;

import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userchat.domain.UserChatEntity;
import java.time.LocalDateTime;

public record UserChatListResponseDTO(
        Long chatId,
        String imageUrl,
        String title,
        String lastMessage,
        LocalDateTime lastCreatedAt,
        Boolean isMute,
        Boolean isFavorite

) {
    public static UserChatListResponseDTO from(UserChatEntity userChatEntity, UserInfoEntity userInfo) {
        return new UserChatListResponseDTO(
                userChatEntity.getChatEntity().getId(),
                userInfo.getImageUrl(),
                userChatEntity.getTitle(),
                userChatEntity.getLastMessage(),
                userChatEntity.getModifiedTime(),
                userChatEntity.getIsMute(),
                userChatEntity.getIsFavorite()
        );
    }
}
