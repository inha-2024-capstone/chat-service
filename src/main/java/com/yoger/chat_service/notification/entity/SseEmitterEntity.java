package com.yoger.chat_service.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@RedisHash("sse_emitter")
@NoArgsConstructor
@AllArgsConstructor
public class SseEmitterEntity {

    private Long userId;

    private SseEmitter sseEmitter;
}
