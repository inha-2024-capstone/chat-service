package com.yoger.chat_service.userchat.mapper;

import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import java.util.List;

public class UserChatEntityMapper {

    public static List<UserChatEntity> createUserChatEntities(List<UserChatCreateRequestDTO> requestDTOs) {
        return requestDTOs.stream().map(
                requestDTO -> {
                    return new UserChatEntity(
                            null,
                            requestDTO.chatEntity(),
                            requestDTO.userId(),
                            requestDTO.receiverId(),
                            requestDTO.receiverName(),
                            null,
                            false,
                            false
                    );
                }
        ).toList();
    }

    public static List<UserChatEntity> updateLastMessage(List<UserChatEntity> userChatEntities, String msg) {
        return userChatEntities.stream().map(userChat -> {
            return new UserChatEntity(
                    userChat.getId(),
                    userChat.getChatEntity(),
                    userChat.getUserId(),
                    userChat.getReceiverId(),
                    userChat.getTitle(),
                    msg,
                    userChat.getIsMute(),
                    userChat.getIsFavorite()
            );
        }).toList();
    }
}
