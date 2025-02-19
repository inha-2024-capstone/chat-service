package com.yoger.chat_service.message.controller;

import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.enums.SuccessStatus;
import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import com.yoger.chat_service.message.dto.request.MessageBatchRequestDTO;
import com.yoger.chat_service.message.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/msg")
@Slf4j
public class MessageController {
    private final MessagingService messagingService;

    @PostMapping("/rcv")
    public ResponseEntity<BaseResponseBody<Void>> receiveMessage(@RequestBody MessageBatchRequestDTO messageBatchRequestDTO) {
        messagingService.sendToSubs(messageBatchRequestDTO);
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getResponseBody());
    }

    @MessageMapping("/msg")
    public void sendMessage(ChatMessageRequestDTO chatMessageRequestDTO) {
        messagingService.sendMessage(chatMessageRequestDTO);
    }
}
