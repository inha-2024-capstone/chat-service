package com.yoger.chat_service.notification.service;

import com.yoger.chat_service.notification.domain.PushEntity;
import com.yoger.chat_service.notification.dto.request.PushReadRequestDTO;
import com.yoger.chat_service.notification.dto.response.ChatPushResponseDTO;
import com.yoger.chat_service.notification.dto.response.ChatPushesResponseDTO;
import com.yoger.chat_service.notification.mapper.PushEntityMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushingService {
    private final PushPersistService pushPersistService;

    public ChatPushesResponseDTO queryAllPushes(Long userId) {
        List<PushEntity> pushEntities = pushPersistService.findByUserId(userId);

        return new ChatPushesResponseDTO(pushEntities.stream().map(ChatPushResponseDTO::from).toList());
    }

    public void readPush(PushReadRequestDTO requestDTO) {
        PushEntity pushEntity = pushPersistService.findByPushId(requestDTO.pushId());
        PushEntity readPush = PushEntityMapper.readPush(pushEntity);
        pushPersistService.save(readPush);
    }
}
