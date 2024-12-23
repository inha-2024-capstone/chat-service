package com.yoger.chat_service.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /ws-stomp 라는 endpoint로 WebSocket 연결
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*")
                .withSockJS();  // SockJS 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 메시지를 보낼 때 붙일 prefix
        config.setApplicationDestinationPrefixes("/pub");

        // 내부 테스트용 임베디드 브로커
        config.enableSimpleBroker("/sub");
    }
}