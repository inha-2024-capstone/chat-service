package com.yoger.chat_service.consumer.message.consumer;

import com.yoger.chat_service.consumer.message.service.ChatRelayService;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final ChatRelayService chatRelayService;
    @KafkaListener(topics = "chat-message", groupId = "chat-group")
    public void consumeMessage(ChatMessageEvent event, Acknowledgment acknowledgment) {
        chatRelayService.relayChatMessage(event);

        acknowledgment.acknowledge();
        log.info("[MSG CONSUMER] commited");
    }
}
