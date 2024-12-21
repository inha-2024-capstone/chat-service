package com.yoger.chat_service.userchat.repository;

import com.yoger.chat_service.userchat.domain.UserChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChatEntity, Long> {
    
}
