package com.yoger.chat_service.notification.repository;

import com.yoger.chat_service.notification.domain.PushEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRepository extends JpaRepository<PushEntity, Long> {

    List<PushEntity> findAllByUserIdOrderByCreateTimeDesc(Long userId);
}
