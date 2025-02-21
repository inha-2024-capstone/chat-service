package com.yoger.chat_service.userchat.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.service.UserInfoQueryService;
import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.dto.request.UserChatDelRequestDTO;
import com.yoger.chat_service.userchat.dto.response.UserChatListResponseDTO;
import com.yoger.chat_service.userchat.mapper.UserChatEntityMapper;
import com.yoger.chat_service.userchat.dto.request.UserChatCreateRequestDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChatService {
    private final UserChatPersistService userChatPersistService;
    private final UserInfoQueryService userInfoQueryService;

    @Transactional(readOnly = false)
    public void createUserChats(List<UserChatCreateRequestDTO> userChatCreateRequestDTO) {
        List<UserChatEntity> userChatEntities = UserChatEntityMapper.createUserChatEntities(userChatCreateRequestDTO);
        userChatPersistService.saveAll(userChatEntities);
    }

    public List<UserChatListResponseDTO> getAllUserChat(Long id) {
        List<UserChatEntity> allByUserId = userChatPersistService.findAllByUserId(id);

        return allByUserId.stream().map(userChatEntity -> {
            UserInfoEntity userInfosById = userInfoQueryService.findById(userChatEntity.getReceiverId());
            return UserChatListResponseDTO.from(userChatEntity, userInfosById);
        }).toList();
    }

    public void deleteUserChat(UserChatDelRequestDTO requestDTO) {
        userChatPersistService.delete(requestDTO.userChatId());
    }

    public Optional<ChatEntity> findChatEntityByUserIds(ChatCreateRequestDTO requestDTO) {
        return userChatPersistService.findChatEntityByUserIds(requestDTO.senderId(), requestDTO.receiverId());
    }

    public void deleteUserChats(List<Long> userIds) {
        userChatPersistService.deleteAll(userIds);
    }
}
