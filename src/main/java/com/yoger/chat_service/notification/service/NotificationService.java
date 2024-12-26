package com.yoger.chat_service.notification.service;

import com.yoger.chat_service.notification.config.NotificationConfig;
import com.yoger.chat_service.notification.dto.response.NotificationResponseDTOs;
import com.yoger.chat_service.notification.entity.NotificationEntity;
import com.yoger.chat_service.notification.entity.SseEmitterEntity;
import com.yoger.chat_service.notification.repository.NotificationRepository;
import com.yoger.chat_service.notification.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String initialDummyData = "initial_dummy_data";

    private final NotificationConfig notificationConfig;

    private final NotificationRepository notificationRepository;

    private final SseEmitterRepository sseEmitterRepository;


    public SseEmitter subscribe(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(notificationConfig.sseEmitterTimeout());
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError(e -> sseEmitter.complete());
        sseEmitter.onCompletion(() -> sseEmitterRepository.deleteById(userId));
        SseEmitterEntity emitterEntity = sseEmitterRepository.save(new SseEmitterEntity(userId, sseEmitter));

        send(userId, initialDummyData, false); // 아무 데이터도 보내지지 않고 연결 종료 시 503 에러 발생

        return emitterEntity.getSseEmitter();
    }

    public void send(Long userId, String content) {
        send(userId, content, true);
    }

    private void send(Long userId, String content, Boolean save) {
        if (save) notificationRepository.save(new NotificationEntity(userId, content));

        sseEmitterRepository.findById(userId).ifPresent(sseEmitterEntity -> {
            SseEmitter sseEmitter = sseEmitterEntity.getSseEmitter();
            try {
                sseEmitter.send(SseEmitter.event().id(userId.toString()).data(content));
            } catch (IOException e) {
                log.error("error occurred while sending message to sseEmitter", e);
                sseEmitter.complete();
            }
        });
    }

    public NotificationResponseDTOs getRecentNotifications(Long userId, Integer count) {
        return NotificationResponseDTOs.from(notificationRepository.findAllByUserIdOrderByCreateTimeDesc(userId, Limit.of(count)));
    }

}
