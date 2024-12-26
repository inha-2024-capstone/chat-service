package com.yoger.chat_service.notification.entity;

import com.yoger.chat_service.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String content;

    private Boolean isRead;

    public NotificationEntity(Long userId, String content) {
        this.userId = userId;
        this.content = content;
        this.isRead = false;
    }

    public void makeRead() {
        this.isRead = true;
    }
}
