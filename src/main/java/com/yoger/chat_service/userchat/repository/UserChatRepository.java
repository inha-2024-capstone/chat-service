package com.yoger.chat_service.userchat.repository;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.userchat.domain.UserChatEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserChatRepository extends JpaRepository<UserChatEntity, Long> {

    List<UserChatEntity> findAllByUserIdAndModifiedTimeIsNotNullOrderByIsFavoriteDescModifiedTimeDesc(Long id);

    List<UserChatEntity> findAllByUserIdOrderByIsFavoriteDescModifiedTimeDesc(Long id);

    void deleteAllByUserIdIn(List<Long> userId);

    List<UserChatEntity> findByChatEntity_Id(Long chatId);

    @Query(
            "SELECT c FROM ChatEntity c "
            + "JOIN UserChatEntity uc ON c.id = uc.chatEntity.id "
            + "WHERE uc.userId = :userId AND uc.receiverId = :receiverId"
    )
    Optional<ChatEntity> findChatByUserAndReceiver(@Param("userId") Long userId, @Param("receiverId") Long receiverId);

}
