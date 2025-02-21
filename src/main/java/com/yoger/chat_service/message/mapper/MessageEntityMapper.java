package com.yoger.chat_service.message.mapper;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.event.ChatMessageEvent;

public class MessageEntityMapper {

    public static MessageEntity createMessage(ChatMessageEvent chatMessageEvent, ChatEntity chatEntity) {
        return new MessageEntity(
                null,
                chatMessageEvent.createdAt(),
                null,
                chatMessageEvent.chatMessage(),
                chatEntity,
                chatMessageEvent.senderId()
        );
    }
}
