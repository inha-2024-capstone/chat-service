package com.yoger.chat_service.userchat.domain;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_chat_table", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "receiver_id"})})
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserChatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "chat_id", updatable = false)
    private ChatEntity chatEntity;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "receiver_id", nullable = false, updatable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String title;

    @Setter
    private String lastMessage;

    @Column(nullable = false)
    private Boolean isMute;

    @Column(nullable = false)
    private Boolean isFavorite;
}
