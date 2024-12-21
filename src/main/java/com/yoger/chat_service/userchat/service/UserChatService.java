package com.yoger.chat_service.userchat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChatService {
    private final UserChatPersistService userChatPersistService;

    @Transactional(readOnly = false)
    public void createUserChat(Long id) {

    }
}
