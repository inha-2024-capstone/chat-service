package com.yoger.chat_service.notification.controller;

import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.enums.SuccessStatus;
import com.yoger.chat_service.notification.dto.request.PushReadRequestDTO;
import com.yoger.chat_service.notification.dto.response.ChatPushesResponseDTO;
import com.yoger.chat_service.notification.service.PushingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push")
public class PushController {
    private final PushingService pushingService;


    @GetMapping("/all")
    public ResponseEntity<BaseResponseBody<ChatPushesResponseDTO>> findAllPushes(
            @RequestHeader(name = "User-Id") Long userId) {
        ChatPushesResponseDTO responseDTO= pushingService.queryAllPushes(userId);
        return SuccessStatus.OK.getResponseBody(responseDTO);
    }

    @MessageMapping("/read")
    public void sendMessage(PushReadRequestDTO requestDTO) {
        pushingService.readPush(requestDTO);
    }
}
