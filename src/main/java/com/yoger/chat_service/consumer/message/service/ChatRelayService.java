package com.yoger.chat_service.consumer.message.service;

import com.yoger.chat_service.consumer.message.handler.MsgBatchHandler;
import com.yoger.chat_service.consumer.message.handler.PushBatchHandler;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import com.yoger.chat_service.message.service.MessagePersistService;
import com.yoger.chat_service.websocket.service.ChatSessionService;
import com.yoger.chat_service.websocket.service.PushSessionService;
import com.yoger.chat_service.websocket.session.value.StompUserSession;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRelayService {
    private final MessagePersistService messagePersistService;
    private final ChatSessionService chatSessionService;
    private final PushSessionService pushSessionService;
    private final PushBatchHandler pushBatchHandler;
    private final MsgBatchHandler msgBatchHandler;

    public void relayChatMessage(ChatMessageEvent event){
        String chatId = String.valueOf(event.chatId());
        List<Long> receiverIds = event.receiverIds();

        Map<String, List<StompUserSession>> chatConnectedSessions = chatSessionService.findConnectedSessions(receiverIds,
                chatId);
        List<Long> chatUnconnectedSessions = chatSessionService.findUnconnectedUserIds(receiverIds, chatId);

        Map<String, List<StompUserSession>> pushConnectedSession = pushSessionService.findConnectedSessions(
                chatUnconnectedSessions);

        // 로깅
        loggingMsg(chatConnectedSessions);
        loggingPush(pushConnectedSession);

        //메시지, 푸쉬 배치 처리 및 RDB 저장
        log.info("MSG BATCH --------------------------------------------");
        msgBatchHandler.batchMsg(event, chatConnectedSessions);
        log.info("PUSH BATCH --------------------------------------------");
        pushBatchHandler.batchChatPush(event, pushConnectedSession);
        log.info("RDS SAVE --------------------------------------------");
        messagePersistService.save(event);
    }

    private void loggingMsg(Map<String, List<StompUserSession>> map) {
        map.forEach((key, value) -> {
            log.info("[MSG SESSION MAP] SERVER IP={}", key);
            value.forEach(v -> {
                log.info("[MSG SESSION MAP] USER ID={}, SESSION ID={}", v.userId(), v.sessionId());
            });
        });
    }

    private void loggingPush(Map<String, List<StompUserSession>> map) {
        map.forEach((key, value) -> {
            log.info("[PUSH SESSION MAP] SERVER IP={}", key);
            value.forEach(v -> {
                log.info("[PUSH SESSION MAP] USER ID={}, SESSION ID={}", v.userId(), v.sessionId());
            });
        });
    }
}
