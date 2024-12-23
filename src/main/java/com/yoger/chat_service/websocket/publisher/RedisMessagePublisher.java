package com.yoger.chat_service.websocket.publisher;

import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import com.yoger.chat_service.message.dto.response.ChatMessageResponseDTO;
import com.yoger.chat_service.websocket.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * "chat-message" 채널로 메시지를 발행
     */
    public void publish(ChatMessage chatMessage) {
        redisTemplate.convertAndSend("chat-message", chatMessage);
    }
}
