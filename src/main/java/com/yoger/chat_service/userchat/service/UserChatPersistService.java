package com.yoger.chat_service.userchat.service;

import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.repository.UserChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserChatPersistService {
    private final UserChatRepository userChatRepository;

    public UserChatEntity findById(Long id) {
        return userChatRepository.findById(id).orElseThrow(() -> new RuntimeException("id에 해당하는 userChat이 존재하지 않습니다."));
    }

    public UserChatEntity save(UserChatEntity userChatEntity) {
        return userChatRepository.save(userChatEntity);
    }
}
