package com.yoger.chat_service.notification.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("notification")
public record NotificationConfig(@NotNull Long sseEmitterTimeout) {
}
