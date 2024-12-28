package com.yoger.chat_service.message.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.service.ChatPersistService;
import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import com.yoger.chat_service.message.dto.response.ChatMessageResponseDTO;
import com.yoger.chat_service.message.mapper.MessageEntityMapper;
import com.yoger.chat_service.websocket.dto.ChatMessage;
import com.yoger.chat_service.websocket.publisher.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessagePersistService messagePersistService;
    private final ChatPersistService chatPersistService;
    private final RedisMessagePublisher redisPublisher;

    @Transactional(readOnly = false)
    public ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO chatMessageRequestDTO) {
        ChatEntity chatEntity = chatPersistService.findById(chatMessageRequestDTO.chatId());
        MessageEntity messageEntity = MessageEntityMapper.toMessageEntity(chatMessageRequestDTO, chatEntity);
        MessageEntity saved = messagePersistService.save(messageEntity);
        redisPublisher.publish(ChatMessage.from(chatMessageRequestDTO, saved));
        return ChatMessageResponseDTO.from(saved);
    }
}
