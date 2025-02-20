package com.yoger.chat_service.notification.service;

import com.yoger.chat_service.notification.domain.PushEntity;
import com.yoger.chat_service.notification.repository.PushRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushPersistService {

    private final PushRepository pushRepository;

    public List<PushEntity> findByUserId(Long userId) {
        return pushRepository.findAllByUserIdOrderByCreateTimeDesc(userId);
    }

    public List<PushEntity> saveAll(List<PushEntity> pushEntities) {
        return pushRepository.saveAll(pushEntities);
    }

    public PushEntity save(PushEntity pushEntity) {
        return pushRepository.save(pushEntity);
    }

    public PushEntity findByPushId(Long pushId) {
        return pushRepository.findById(pushId).orElseThrow(() -> new RuntimeException("해당 ID의 push가 존재하지 않습니다."));
    }
}
