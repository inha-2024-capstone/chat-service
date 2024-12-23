package com.yoger.chat_service.message.controller;

import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import com.yoger.chat_service.message.dto.response.ChatMessageResponseDTO;
import com.yoger.chat_service.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/chat/message")
    @SendTo("/sub/chat/room/{chatId}")
    public ChatMessageResponseDTO message(ChatMessageRequestDTO chatMessageRequestDTO) {
        // 받은 메시지를 Redis Pub/Sub으로 발행
        return messageService.sendMessage(chatMessageRequestDTO);
    }
}
