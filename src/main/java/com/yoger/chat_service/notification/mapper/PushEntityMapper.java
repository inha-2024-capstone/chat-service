package com.yoger.chat_service.notification.mapper;

import com.yoger.chat_service.notification.domain.PushEntity;
import com.yoger.chat_service.notification.domain.PushType;
import com.yoger.chat_service.notification.dto.request.ChatPushBatchRequestDTO;
import java.util.List;

public class PushEntityMapper {

    public static List<PushEntity> createPushes(ChatPushBatchRequestDTO requestDTO) {
        List<Long> receiverIds = requestDTO.sessionMap().keySet().stream().toList();
        return receiverIds.stream().map(id ->
                new PushEntity(
                        null,
                        id,
                        requestDTO.chatId(),
                        requestDTO.chatMessage(),
                        false,
                        PushType.Chat
                )).toList();
    }

    public static PushEntity readPush(PushEntity pushEntity) {
        return new PushEntity(
                pushEntity.getId(),
                pushEntity.getUserId(),
                pushEntity.getEventId(),
                pushEntity.getMessage(),
                true,
                pushEntity.getType()
        );
    }
}
