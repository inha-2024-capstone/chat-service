package com.yoger.chat_service.userchat.controller;

import com.yoger.chat_service.common.response.BaseResponseBody;
import com.yoger.chat_service.common.status.enums.SuccessStatus;
import com.yoger.chat_service.userchat.dto.request.UserChatDelRequestDTO;
import com.yoger.chat_service.userchat.dto.response.UserChatListResponseDTO;
import com.yoger.chat_service.userchat.service.UserChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-chat")
public class UserChatController {
    private final UserChatService userChatService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseBody<List<UserChatListResponseDTO>>> getAllUserChats(
            @RequestHeader("User-Id") Long userId) {

        List<UserChatListResponseDTO> allUserChat = userChatService.getAllUserChat(userId);
        return SuccessStatus.OK.getResponseBody(allUserChat);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseBody<Void>> deleteUserChat(@RequestBody UserChatDelRequestDTO requestDTO) {

        userChatService.deleteUserChat(requestDTO);
        return SuccessStatus.OK.getResponseBody();
    }
}
