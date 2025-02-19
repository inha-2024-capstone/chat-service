package com.yoger.chat_service.common.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
@Setter
public class SelfServerUrl {
    @Value("${self-server-url}")
    private String serverUrl;
}
