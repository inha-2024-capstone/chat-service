package com.yoger.chat_service.consumer.message;

import com.yoger.chat_service.consumer.vo.SessionInfo;
import com.yoger.chat_service.message.dto.request.MessageBatchRequestDTO;
import com.yoger.chat_service.message.event.ChatMessageEvent;
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
public class MsgBatchHandler {

    private final RestClient restClient;
    private static final String MSG_PATH = "/api/msg/rcv";

    private void requestMsg(String serverUrl, MessageBatchRequestDTO requestDTO) {

        ResponseEntity<String> entity = restClient
                .post()
                .uri(serverUrl + MSG_PATH)
                .body(requestDTO).retrieve()
                .toEntity(String.class);

        if (entity.getStatusCode() != HttpStatus.OK) {
            log.info("[MSG BATCH HANDLER] REST API IS NOT OK");
            throw new RuntimeException("메시지 전송이 수행되지 않았습니다.");
        }
    }


    public void batchMsg(ChatMessageEvent event, Map<String, List<StompUserSession>> msgMap) {

        msgMap.forEach((serverUrl, sessionInfos) -> {
            log.info("[MSG BATCH HANDLER] ServerUrl={}", serverUrl);
            Map<Long, SessionInfo> sessionMap = new HashMap<>();
            sessionInfos.forEach(sessionInfo -> {
                sessionMap.put(sessionInfo.userId(), new SessionInfo(sessionInfo.sessionId(), sessionInfo.subId()));
                log.info("[MSG BATCH HANDLER] UserId={}, SessionId={}", sessionInfo.userId(), sessionInfo.sessionId());
            });

            MessageBatchRequestDTO requestDTO = MessageBatchRequestDTO.from(event, sessionMap);
            requestMsg(serverUrl, requestDTO);
        });
    }
}
