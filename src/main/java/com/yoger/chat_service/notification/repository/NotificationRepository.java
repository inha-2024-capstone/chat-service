package com.yoger.chat_service.notification.repository;

import com.yoger.chat_service.notification.entity.NotificationEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findAllByUserIdOrderByCreateTimeDesc(Long userId, Limit limit);
}
