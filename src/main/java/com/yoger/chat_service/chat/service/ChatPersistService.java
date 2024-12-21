package com.yoger.chat_service.chat.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPersistService {
    private final ChatRepository chatRepository;

    public ChatEntity save(ChatEntity chatEntity) {
        return chatRepository.save(chatEntity);
    }

    public ChatEntity findById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 ID에 매핑되는 채팅방이 없습니다."));
    }
}
