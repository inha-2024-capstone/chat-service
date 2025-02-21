package com.yoger.chat_service.websocket.listener;

import com.yoger.chat_service.websocket.repository.InMemoryStompSessionStore;
import com.yoger.chat_service.websocket.service.PushSessionService;
import com.yoger.chat_service.websocket.session.key.ChatSessionKey;
import com.yoger.chat_service.websocket.service.ChatSessionService;
import com.yoger.chat_service.websocket.session.key.PushSessionKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompSubEventListener {

    private final ChatSessionService chatSessionService;
    private final PushSessionService pushSessionService;
    private final InMemoryStompSessionStore inMemoryStompSessionStore;

    private final static String CHAT_SUB_PREFIX = "/sub/chat/";
    private final static String PUSH_SUB_PREFIX = "/sub/push/";

    private final static String CHAT_SUB_ID_PREFIX = "chat";
    private final static String PUSH_SUB_ID_PREFIX = "push";

    private void validateDst(String destination) {
        if (destination == null) {
            throw new RuntimeException("destination이 null입니다.");
        }
    }

    private void validateSubId(String subId) {
        if (subId == null) {
            throw new RuntimeException("Sub Id가 null입니다.");
        }
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();
        String subId = headerAccessor.getSubscriptionId();

        validateSubId(subId);

        // 둘 중 하나에 존재하면 삭제함.
        if (subId.startsWith(CHAT_SUB_ID_PREFIX)) {
            chatSessionService.unSubscribe(sessionId, subId);
        } else if (subId.startsWith(PUSH_SUB_ID_PREFIX)) {
            pushSessionService.unSubscribe(sessionId, subId);
        }

        log.info("[WEBSOCKET UNSUBSCRIBE] sessionId={}, subId={}", sessionId, subId);
    }

    @EventListener
    public void handleSubscriptionEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();

        validateDst(destination);

        String sessionId = headerAccessor.getSessionId();

        // Subscription Header 에 "id"를 전달
        String subId = headerAccessor.getSubscriptionId();

        log.info("[SUBSCRIBE] Destination: {}", destination);

        if (destination.startsWith(CHAT_SUB_PREFIX)) {
            Long userId = inMemoryStompSessionStore.findUserIdBySessionId(sessionId);
            String chatId = destination.replaceFirst(CHAT_SUB_PREFIX, "");

            ChatSessionKey chatSessionKey = new ChatSessionKey(chatId, userId);
            storeChatSession(chatSessionKey, sessionId, subId);

        } else if (destination.startsWith(PUSH_SUB_PREFIX)) {
            Long userId = inMemoryStompSessionStore.findUserIdBySessionId(sessionId);

            PushSessionKey pushSessionKey = new PushSessionKey(userId);
            storePushSession(pushSessionKey, sessionId, subId);
        }

    }

    private void storeChatSession(ChatSessionKey chatSessionKey, String sessionId, String subId) {
        chatSessionService.subscribe(chatSessionKey, sessionId, subId);
        log.info("[CHAT SUBSCRIBE] key={}, sessionId={}, subscribed well", chatSessionKey.getSessionKey(),
                sessionId);
    }

    private void storePushSession(PushSessionKey pushSessionKey, String sessionId, String subId) {
        pushSessionService.subscribe(pushSessionKey, sessionId, subId);
        log.info("[PUSH SUBSCRIBE] key={}, sessionId={}, subscribed well", pushSessionKey.getSessionKey(),
                sessionId);
    }

}