package com.yoger.chat_service.websocket.publisher;

import com.yoger.chat_service.websocket.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChatMessage chatMessage) {
        redisTemplate.convertAndSend("chat-message", chatMessage);
    }
}
