package com.yoger.chat_service.userchat.service;

import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.mapper.UserChatEntityMapper;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChatService {
    private final UserChatPersistService userChatPersistService;

    @Transactional(readOnly = false)
    public void createUserChat(UserChatCreateRequestDTO userChatCreateRequestDTO) {
        UserChatEntity userChatEntity = UserChatEntityMapper.toUserChatEntity(userChatCreateRequestDTO);
        userChatPersistService.save(userChatEntity);
    }
}
