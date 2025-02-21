package com.yoger.chat_service.message.service;

import com.yoger.chat_service.consumer.vo.SessionInfo;
import com.yoger.chat_service.message.dto.request.ChatMessageRequestDTO;
import com.yoger.chat_service.message.dto.request.MessageBatchRequestDTO;
import com.yoger.chat_service.message.dto.response.ChatMessageResponseDTO;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import com.yoger.chat_service.notification.dto.request.ChatPushBatchRequestDTO;
import com.yoger.chat_service.notification.service.PushingService;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.service.UserInfoQueryService;
import com.yoger.chat_service.websocket.repository.ChatSessionService;
import com.yoger.chat_service.websocket.session.key.ChatSessionKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagingService {
    private final MessageProduceService messageProduceService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatSessionService chatSessionService;
    private final PushingService pushingService;
    private final UserInfoQueryService userInfoQueryService;

    private final static String CHAT_SUB_DST = "/sub/chat/";

    @Transactional(readOnly = false)
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
        validateConnection(requestDTO);
        log.info("[SERVICE] 3 Validation Went Well");
    }

    private void validateConnection(MessageBatchRequestDTO requestDTO) {
        String chatId = String.valueOf(requestDTO.chatId());
        Map<Long, SessionInfo> sessionMap = requestDTO.sessionMap();
        // 현재 서버와 연결되지 않은 클라이언트 들은 Push 를 보내줘야 함. batch 를 위한 sessionMap
        Map<Long, SessionInfo> pushSessionMap = new HashMap<>();
        sessionMap.forEach((receiverId, sessionInfo) -> {
            ChatSessionKey key = new ChatSessionKey(chatId, receiverId);
            String sessionId = sessionInfo.sessionId();
            String subId = sessionInfo.subId();

            if (!chatSessionService.isInternallyConnected(sessionId, subId)) {
                log.info("[REST API FOR SENDING] {} id의 사용자는 websocket에 연결되지 않았습니다.\n"
                        + "DELETING sessionId={}, key={}", receiverId, sessionId, key.getSessionKey());
                chatSessionService.unSubscribe(sessionId, subId);
                pushSessionMap.put(receiverId, new SessionInfo(sessionId, subId));
            }
        });
        if (!pushSessionMap.isEmpty()) {
            batchChatPush(requestDTO, pushSessionMap);
        }
    }

    private void batchChatPush(MessageBatchRequestDTO requestDTO, Map<Long, SessionInfo> pushSessionMap) {
        ChatPushBatchRequestDTO chatPushBatchRequestDTO = ChatPushBatchRequestDTO.from(requestDTO, pushSessionMap,
                createPushMessage(requestDTO.message(), requestDTO.senderId()));
        pushingService.batchChatPush(chatPushBatchRequestDTO);
    }

    public String createPushMessage(String msg, Long userId) {
        UserInfoEntity userInfoEntity = userInfoQueryService.findById(userId);

        String userName = userInfoEntity.getUserName();
        return userName + ":" + msg;
    }
}
