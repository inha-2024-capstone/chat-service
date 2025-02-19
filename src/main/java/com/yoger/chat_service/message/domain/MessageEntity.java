package com.yoger.chat_service.message.domain;

import com.yoger.chat_service.chat.domain.ChatEntity;
import com.yoger.chat_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "message_table")
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    private Long id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatEntity chatEntity;

    private Long senderId;

    public MessageEntity(LocalDateTime createTime, LocalDateTime modifiedTime, Long id, String message,
                         ChatEntity chatEntity, Long senderId) {
        super(createTime, modifiedTime);
        this.id = id;
        this.message = message;
        this.chatEntity = chatEntity;
        this.senderId = senderId;
    }
}
