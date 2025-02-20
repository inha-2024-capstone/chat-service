package com.yoger.chat_service.chat.controller;

import com.yoger.chat_service.chat.dto.request.ChatCreateRequestDTO;
import com.yoger.chat_service.chat.dto.response.ChatRoomResponseDTO;
import com.yoger.chat_service.chat.service.ChatService;
import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.enums.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<BaseResponseBody<ChatRoomResponseDTO>> createChatRoom(
            @RequestBody ChatCreateRequestDTO chatCreateRequestDTO) {
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getResponseBody(chatService.createChat(chatCreateRequestDTO)));
    }

    @GetMapping("/get/{chatId}")
    public ResponseEntity<BaseResponseBody<ChatRoomResponseDTO>> createChatRoom(@PathVariable(name = "chatId") Long chatId) {
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getResponseBody(chatService.getChat(chatId)));
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<BaseResponseBody<Void>> deleteChatRoom(@PathVariable(name = "chatId") Long chatId) {
        chatService.deleteChatRoom(chatId);
        return ResponseEntity
                .status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getResponseBody());
    }
}
