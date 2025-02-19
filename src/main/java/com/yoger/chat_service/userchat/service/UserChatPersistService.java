package com.yoger.chat_service.userchat.service;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.userchat.domain.UserChatEntity;
import com.yoger.chat_service.userchat.mapper.UserChatEntityMapper;
import com.yoger.chat_service.userchat.repository.UserChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserChatPersistService {
    private final UserChatRepository userChatRepository;

    public List<UserChatEntity> findAllByUserId(Long id) {
        return userChatRepository.findAllByUserIdAndModifiedTimeIsNotNullOrderByIsFavoriteDescModifiedTimeDesc(id);
    }

    public List<UserChatEntity> findAllByUserIdTest(Long id) {
        return userChatRepository.findAllByUserIdOrderByIsFavoriteDescModifiedTimeDesc(id);
    }

    public void modifyLastChat(Long chatId, String message) {
        List<UserChatEntity> userChatEntities = userChatRepository.findByChatEntity_Id(chatId);

        List<UserChatEntity> updatedEntities = UserChatEntityMapper.updateLastMessage(userChatEntities, message);

        userChatRepository.saveAll(updatedEntities);
    }

    public List<UserChatEntity> saveAll(List<UserChatEntity> userChatEntity) {
        return userChatRepository.saveAll(userChatEntity);
    }

    public Optional<ChatEntity> findChatEntityByUserIds(Long userId, Long receiverId) {
        return userChatRepository.findChatByUserAndReceiver(userId, receiverId);
    }

    public void delete(Long id) {
        userChatRepository.deleteById(id);
    }

    public void deleteAll(List<Long> userIds) {
        userChatRepository.deleteAllByUserIdIn(userIds);
    }
}
