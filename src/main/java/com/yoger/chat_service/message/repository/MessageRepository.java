package com.yoger.chat_service.message.repository;

import com.yoger.chat_service.message.domain.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
