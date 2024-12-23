package com.yoger.chat_service.chat.controller;

import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.chat.dto.response.ChatCreateResponseDTO;
import com.yoger.chat_service.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ChatCreateResponseDTO createChatRoom(@RequestBody ChatCreateRequestDTO chatCreateRequestDTO) {
        log.info("userIds={}", chatCreateRequestDTO);
        return chatService.createChat(chatCreateRequestDTO);
    }
}
