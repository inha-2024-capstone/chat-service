package com.yoger.chat_service.consumer.message.consumer;

import com.yoger.chat_service.common.constant.KafkaConstant;
import com.yoger.chat_service.consumer.message.service.ChatRelayService;
import com.yoger.chat_service.message.event.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final ChatRelayService chatRelayService;

    @KafkaListener(topics = KafkaConstant.MSG_TOPIC, groupId = KafkaConstant.CHAT_GROUP_ID)
    public void consumeMessage(ChatMessageEvent event, Acknowledgment acknowledgment) {
        chatRelayService.relayChatMessage(event);

        acknowledgment.acknowledge();
        log.info("[MSG CONSUMER] commited");
    }
}
