package com.yoger.chat_service.message.service;

import com.yoger.chat_service.message.event.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProduceService {
    private final KafkaTemplate<String, ChatMessageEvent> kafkaTemplate;
    private static final String topicName = "chat-message";

    public void sendMessage(ChatMessageEvent chatMessageEvent) {
        kafkaTemplate
                .send(topicName, String.valueOf(chatMessageEvent.chatId()), chatMessageEvent)
                .thenAcceptAsync(result -> {
                    RecordMetadata recordMetadata = result.getRecordMetadata();
                    ProducerRecord<String, ChatMessageEvent> producerRecord = result.getProducerRecord();
                    log.info("[PRODUCER] topic={}, partition={}, offset={}, event={}", recordMetadata.topic(), recordMetadata.partition(),
                            recordMetadata.offset(), producerRecord.value().chatMessage());})
                .exceptionallyAsync(result -> {
                    throw new RuntimeException(result.getMessage() + "\n 채팅 메시지 전송에 실패했습니다.");
        });
    }
}
