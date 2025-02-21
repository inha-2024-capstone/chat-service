package com.yoger.chat_service.notification.domain;

import com.yoger.chat_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "push_table")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PushEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_id")
    private Long id;

    private Long userId;

    private Long eventId;

    private String message;

    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    private PushType type;
}
