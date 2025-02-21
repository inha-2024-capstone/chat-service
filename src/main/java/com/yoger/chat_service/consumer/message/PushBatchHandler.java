package com.yoger.chat_service.consumer.message;

import com.yoger.chat_service.consumer.vo.SessionInfo;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import com.yoger.chat_service.notification.dto.request.ChatPushBatchRequestDTO;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.service.UserInfoQueryService;
import com.yoger.chat_service.websocket.session.value.StompUserSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushBatchHandler {
    private final RestClient restClient;
    private final UserInfoQueryService userInfoQueryService;
    private static final String PUSH_PATH = "/api/push/chat";

    private void requestPush(String serverUrl, ChatPushBatchRequestDTO requestDTO) {

        ResponseEntity<String> entity = restClient
                .post()
                .uri(serverUrl + PUSH_PATH)
                .body(requestDTO)
                .retrieve()
                .toEntity(String.class);

        if (entity.getStatusCode() != HttpStatus.OK) {
            log.info("[PUSH BATCH HANDLER] REST API IS NOT OK");
            throw new RuntimeException("푸쉬 전송이 수행되지 않았습니다.");
        }
    }

    public void batchChatPush(ChatMessageEvent event, Map<String, List<StompUserSession>> pushMap) {
        pushMap.forEach((serverUrl, sessionInfos) -> {
            log.info("[PUSH BATCH HANDLER] ServerUrl={}", serverUrl);
            Map<Long, SessionInfo> sessionMap = new HashMap<>();

            sessionInfos.forEach(sessionInfo -> {
                sessionMap.put(sessionInfo.userId(), new SessionInfo(sessionInfo.sessionId(), sessionInfo.subId()));
                log.info("[PUSH BATCH HANDLER] UserId={}, SessionId={}", sessionInfo.userId(), sessionInfo.sessionId());
            });
            String pushMessage = createPushMessage(event, event.senderId());
            ChatPushBatchRequestDTO from = ChatPushBatchRequestDTO.from(event, sessionMap, pushMessage);
            requestPush(serverUrl, from);
        });
    }

    public String createPushMessage(ChatMessageEvent event, Long userId) {
        UserInfoEntity userInfoEntity = userInfoQueryService.findById(userId);

        String userName = userInfoEntity.getUserName();
        return userName + ":" + event.chatMessage();
    }
}
