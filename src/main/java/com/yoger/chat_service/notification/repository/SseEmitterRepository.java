package com.yoger.chat_service.notification.repository;

import com.yoger.chat_service.notification.entity.SseEmitterEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {

    private final Map<Long, SseEmitterEntity> sseEmitterRepository = new ConcurrentHashMap<>();

    public SseEmitterEntity save(SseEmitterEntity sseEmitterEntity) {
        return sseEmitterRepository.put(sseEmitterEntity.getUserId(), sseEmitterEntity);
    }

    public Optional<SseEmitterEntity> findById(Long userId) {
        return Optional.ofNullable(sseEmitterRepository.get(userId));
    }

    public void deleteById(Long userId) {
        sseEmitterRepository.remove(userId);
    }
}
