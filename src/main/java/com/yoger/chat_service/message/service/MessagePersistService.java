package com.yoger.chat_service.message.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.service.ChatPersistService;
import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import com.yoger.chat_service.message.mapper.MessageEntityMapper;
import com.yoger.chat_service.message.repository.MessageRepository;
import com.yoger.chat_service.userchat.service.UserChatPersistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePersistService {
    private final MessageRepository messageRepository;
    private final ChatPersistService chatPersistService;
    private final UserChatPersistService userChatPersistService;

    @Transactional
    public MessageEntity save(ChatMessageEvent chatMessageEvent) {
        ChatEntity chatEntity = chatPersistService.findById(chatMessageEvent.chatId());
        MessageEntity messageEntity = MessageEntityMapper.createMessage(chatMessageEvent, chatEntity);

        userChatPersistService.modifyLastChat(chatMessageEvent.chatId(), chatMessageEvent.chatMessage());
        return messageRepository.save(messageEntity);
    }
}
