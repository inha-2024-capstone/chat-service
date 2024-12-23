package com.yoger.chat_service.websocket.config;

import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ServerId {
    private final String serverId;

    public ServerId() {
        this.serverId = UUID.randomUUID().toString();
    }
}
