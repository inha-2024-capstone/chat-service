package com.yoger.chat_service.chat.dto.response;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import java.util.List;

public record ChatRoomResponseDTO(
        Long chatId,
        List<ChatMessage> messages,
        List<ChatUserInfo> chatUserInfos
) {
    public static ChatRoomResponseDTO from(ChatEntity chatEntity, List<UserInfoEntity> userInfoEntities) {
        List<ChatUserInfo> chatUserInfos = userInfoEntities.stream().map(ChatUserInfo::from).toList();
        List<ChatMessage> chatMessages = null;
        if(chatEntity.getMessageEntities() != null) {
            chatMessages = chatEntity.getMessageEntities().stream().map(ChatMessage::from).toList();
        }
        return new ChatRoomResponseDTO(
                chatEntity.getId(),
                chatMessages,
                chatUserInfos
        );
    }
}