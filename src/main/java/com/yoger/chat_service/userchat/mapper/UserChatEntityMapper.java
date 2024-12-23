package com.yoger.chat_service.userchat.mapper;

import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;

public class UserChatEntityMapper {

    public static UserChatEntity toUserChatEntity(UserChatCreateRequestDTO userChatCreateRequestDTO) {
        return new UserChatEntity(
                null,
                userChatCreateRequestDTO.chatEntity(),
                userChatCreateRequestDTO.id(),
                false,
                false);
    }
}
