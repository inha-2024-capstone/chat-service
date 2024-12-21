package com.yoger.chat_service.chat.repository;

import com.yoger.chat_service.chat.domain.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

}
