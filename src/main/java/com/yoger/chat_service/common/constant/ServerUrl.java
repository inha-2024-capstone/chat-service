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
public class ServerUrl {
    @Value("${spring.kafka.SERVER-IP}")
    private String serverUrl;
}
