package com.yoger.chat_service.websocket.subscriber;

import com.yoger.chat_service.websocket.config.ServerId;
import com.yoger.chat_service.websocket.dto.ChatMessage;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpUserRegistry simpUserRegistry; // 현재 서버의 STOMP 세션/사용자 조회
    private final String myServerId; // 현재 서버 ID (예: UUID, 호스트명 등)

    public RedisMessageSubscriber(SimpMessageSendingOperations messagingTemplate,
                                  RedisTemplate<String, Object> redisTemplate, SimpUserRegistry simpUserRegistry,
                                  ServerId serverId) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
        this.simpUserRegistry = simpUserRegistry;
        this.myServerId = serverId.getServerId();
    }

    /**
     * Redis Pub/Sub로부터 메시지 수신 시 호출되는 메서드
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // (1) message.getBody()를 ChatMessage로 역직렬화
        ChatMessage chatMessage = (ChatMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());

        if (chatMessage == null) {
            log.warn("[SUBSCRIBER] Received null or invalid message. Skip.");
            return;
        }

        String chatId = chatMessage.chatId().toString();
        List<Long> receiverIds = chatMessage.receiverIds();

        // (2) Redis에서 "chatId:receiverId" 조회 → {serverId, sessionId}
        receiverIds.forEach(receiverId -> {
            String key = chatId + ":" + receiverId.toString();
            Map<Object, Object> sessionMap = redisTemplate.opsForHash().entries("WS_SESSION");
            @SuppressWarnings("unchecked") Map<String, String> sessionValue = (Map<String, String>) sessionMap.get(key);

            // 해당 user가 현재 연결 정보가 있는지 확인
            if (sessionValue == null) {
                log.info("[SUBSCRIBER] No active session found for key={}", key);
                return;
            }

            String targetServerId = sessionValue.get("serverId");
            String targetSessionId = sessionValue.get("sessionId");

            // (3) 이 메시지를 처리해야 하는 서버가 '나'인지 확인
            if (!myServerId.equals(targetServerId)) {
                // 내 서버 ID와 다르면, 내가 처리할 메시지가 아님
                log.info("[SUBSCRIBER] This message is for serverId={}, but I'm {}. Skip.", targetServerId, myServerId);
                return;
            }

            // (4) 세션이 정말 연결되어 있는지 STOMP UserRegistry로 확인
            //     - receiverId를 Principal name으로 매핑했다고 가정하는 예시
            //     - (프로젝트마다 userId -> principalName 매핑 로직이 다를 수 있음)
            boolean isConnected = checkUserConnected(receiverId.toString());

            if (!isConnected) {
                log.info("[SUBSCRIBER] User {} not connected on this server. Skip sending message.", receiverId);
                return;
            }

            // (5) 모든 조건이 OK면 메시지 전송
            String destination = "/sub/chat/room/" + chatId;
            messagingTemplate.convertAndSend(destination, chatMessage);

            log.info(
                    "[SUBSCRIBER] Received ChatMessage. Delivered to user={} (sessionId={}) on server={} destination={}",
                    receiverId, targetSessionId, myServerId, destination);
        });
    }

    /**
     * "이 서버"에 userId(혹은 Principal Name)가 연결되어 있는지 검사하는 예시
     */
    private boolean checkUserConnected(String userId) {
        // simpUserRegistry에서 user를 찾아본다.
        // 단, userId가 Principal name과 1:1로 대응된다고 가정해야 함
        SimpUser simpUser = simpUserRegistry.getUser(userId);
        if (simpUser == null) {
            // 해당 사용자 Principal이 아예 없으면 연결되지 않은 것
            return false;
        }

        // 이 유저가 가진 STOMP session이 하나라도 살아있으면 연결 상태
        return !simpUser.getSessions().isEmpty();
    }
}
