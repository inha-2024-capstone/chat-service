package com.yoger.chat_service.websocket.subscriber;

import com.yoger.chat_service.websocket.config.ServerId;
import com.yoger.chat_service.websocket.dto.ChatMessage;
import com.yoger.chat_service.websocket.session.StompSessionKey;
import com.yoger.chat_service.websocket.session.StompSessionStore;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final StompSessionStore stompSessionStore;
    private final String myServerId;

    private static final String DESTINATION_PREFIX = "/sub/chat/room/";

    public RedisMessageSubscriber(SimpMessageSendingOperations messagingTemplate,
                                  RedisTemplate<String, Object> redisTemplate, StompSessionStore stompSessionStore,
                                  ServerId serverId) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
        this.stompSessionStore = stompSessionStore;
        this.myServerId = serverId.getServerId();
    }

    /**
     * Redis Pub/Sub로부터 메시지 수신 시 호출되는 메서드
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        ChatMessage chatMessage = (ChatMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());

        if (chatMessage == null) {
            log.warn("[SUBSCRIBER] ChatMessage가 null입니다.");
            return;
        }

        Map<Object, Object> sessionMap = redisTemplate.opsForHash().entries("WS_SESSION");

        processChatMessage(chatMessage, sessionMap);
    }

    private void processChatMessage(ChatMessage chatMessage, Map<Object, Object> sessionMap) {
        chatMessage.receiverIds().forEach(receiverId -> {

            StompSessionKey stompSessionKey = new StompSessionKey(chatMessage.chatId().toString(),
                    receiverId.toString());

            @SuppressWarnings("unchecked") Map<String, String> sessionValue = (Map<String, String>) sessionMap.get(
                    stompSessionKey.getSessionKey());

            if (sessionValue == null) {
                log.info("[SUBSCRIBER] No active session found for key={}", stompSessionKey.getSessionKey());
                return;
            }

            String targetServerId = sessionValue.get("serverId");
            String targetSessionId = sessionValue.get("sessionId");

            if (!myServerId.equals(targetServerId)) {
                log.info("[SUBSCRIBER] This message is for serverId={}, but I'm {}. Skip.", targetServerId, myServerId);
                return;
            }

            if (!checkUserConnected(stompSessionKey, targetSessionId)) {
                log.info("[SUBSCRIBER] User {} not connected on this server. Skip sending message.", receiverId);
                return;
            }

            sendChatMessage(chatMessage, chatMessage.chatId().toString(), receiverId);
        });
    }

    private void sendChatMessage(ChatMessage chatMessage, String chatId, Long receiverId) {
        String destination = DESTINATION_PREFIX + chatId;
        messagingTemplate.convertAndSend(destination, chatMessage);

        log.info("[SUBSCRIBER] {} 채팅방의 {} 유저에게 메시지가 전달", chatId, receiverId);
    }

    private boolean checkUserConnected(StompSessionKey stompSessionKey, String sessionId) {
        return stompSessionStore.isConnected(stompSessionKey, sessionId);
    }
}
