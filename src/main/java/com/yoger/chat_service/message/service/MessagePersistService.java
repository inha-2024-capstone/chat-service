package com.yoger.chat_service.message.service;

import com.yoger.chat_service.message.domain.MessageEntity;
import com.yoger.chat_service.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagePersistService {
    private final MessageRepository messageRepository;

    public MessageEntity findById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id에 매핑되는 message가 존재하지 않습니다."));

    }

    public MessageEntity save(MessageEntity messageEntity) {
        return messageRepository.save(messageEntity);
    }
}
