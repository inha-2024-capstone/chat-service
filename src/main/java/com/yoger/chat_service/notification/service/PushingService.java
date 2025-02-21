package com.yoger.chat_service.notification.service;

import com.yoger.chat_service.notification.domain.PushEntity;
import com.yoger.chat_service.notification.dto.request.ChatPushBatchRequestDTO;
import com.yoger.chat_service.notification.dto.request.PushReadRequestDTO;
import com.yoger.chat_service.notification.dto.response.ChatPushResponseDTO;
import com.yoger.chat_service.notification.dto.response.ChatPushesResponseDTO;
import com.yoger.chat_service.notification.mapper.PushEntityMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = false)
public class PushingService {
    private final PushPersistService pushPersistService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final static String WS_SUB_DST = "/sub/push/";

    public void batchChatPush(ChatPushBatchRequestDTO requestDTO) {

        List<PushEntity> pushes = PushEntityMapper.createPushes(requestDTO);
        List<PushEntity> pushEntities = pushPersistService.saveAll(pushes);

        ChatPushResponseDTO responseDTO = ChatPushResponseDTO.from(pushEntities.getFirst());

        pushEntities.forEach(push -> {
            Long userId = push.getUserId();
            String destination = WS_SUB_DST + userId;
            messagingTemplate.convertAndSend(destination, responseDTO);
            log.info("[Pushing Service] SENT TO {}, With Message {}", destination, responseDTO.message());
        });
    }

    @Transactional(readOnly = true)
    public ChatPushesResponseDTO queryAllPushes(Long userId) {
        List<PushEntity> pushEntities = pushPersistService.findByUserId(userId);

        return ChatPushesResponseDTO.from(pushEntities.stream().map(ChatPushResponseDTO::from).toList());
    }

    public void readPush(PushReadRequestDTO requestDTO) {
        PushEntity pushEntity = pushPersistService.findByPushId(requestDTO.pushId());
        PushEntity readPush = PushEntityMapper.readPush(pushEntity);
        pushPersistService.save(readPush);
    }
}
