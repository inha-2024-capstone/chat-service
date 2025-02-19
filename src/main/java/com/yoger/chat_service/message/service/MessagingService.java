package com.yoger.chat_service.message.service;

import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import com.yoger.chat_service.message.dto.request.MessageBatchRequestDTO;
import com.yoger.chat_service.message.dto.response.ChatMessageResponseDTO;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagingService {
    private final MessageProduceService messageProduceService;
    private final SimpMessageSendingOperations messagingTemplate;

    private final static String CHAT_SUB_DST = "/sub/chat/";

    public void sendMessage(ChatMessageRequestDTO chatMessageRequestDTO) {
        ChatMessageEvent chatMessageEvent = ChatMessageEvent.from(chatMessageRequestDTO);
        messageProduceService.sendMessage(chatMessageEvent);
    }

    public void sendToSubs(MessageBatchRequestDTO requestDTO) {
        String destination = CHAT_SUB_DST + requestDTO.chatId();

        log.info("[SERVICE] 1 TRYING TO SEND MSG");
        // 구독중인 클라이언트에게 BroadCast
        ChatMessageResponseDTO chatMessageResponseDTO = ChatMessageResponseDTO.from(requestDTO);
        messagingTemplate.convertAndSend(destination, chatMessageResponseDTO);
        //logging
        List<Long> receiverIds = requestDTO.sessionMap().keySet().stream().toList();
        log.info("[SERVICE] 2 MSG SENT GOOD, Msg={}, UserIds={}", requestDTO.message(), receiverIds);
        //현재 연결되지 않은 클라이언트들에 대해 검증 및 푸쉬 발송
        log.info("[SERVICE] 3 Validation Went Well");
    }
}
