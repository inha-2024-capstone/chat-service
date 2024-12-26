package com.yoger.chat_service.notification.controller;

import com.yoger.chat_service.notification.dto.response.NotificationResponseDTOs;
import com.yoger.chat_service.event.ChatUserNotPresentEvent;
import com.yoger.chat_service.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@RequestHeader("User-Id") Long userId) {
        return new ResponseEntity<>(notificationService.subscribe(userId), HttpStatus.OK);
    }

    @KafkaListener(topics = "notification", groupId = "notification-group",
            containerFactory = "chatUserNotPresentEventContainerFactory")
    public void notificationEvent(ChatUserNotPresentEvent event) {
        notificationService.send(event.getUserId(), event.getMessage());
    }

    @GetMapping("/recent/count/{count}")
    public ResponseEntity<NotificationResponseDTOs> getRecentNotifications(@RequestHeader("User-Id") Long userId, @PathVariable Integer count) {
        return new ResponseEntity<>(notificationService.getRecentNotifications(userId, count), HttpStatus.OK);
    }
}
