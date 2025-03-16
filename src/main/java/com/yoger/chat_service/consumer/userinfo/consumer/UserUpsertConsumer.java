package com.yoger.chat_service.consumer.userinfo.consumer;

import com.mog.authserver.auth.event.UserUpsertEvent;
import com.yoger.chat_service.common.constant.KafkaConstant;
import com.yoger.chat_service.consumer.userinfo.service.UserUpsertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUpsertConsumer {

    private final UserUpsertService userUpsertService;

    @KafkaListener(topics = KafkaConstant.USER_TOPIC, groupId = KafkaConstant.USER_GROUP_ID)
    public void consumeMessage(UserUpsertEvent event, Acknowledgment acknowledgment) {
        userUpsertService.saveUserInfo(event);
        acknowledgment.acknowledge();
    }
}
