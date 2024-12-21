package com.yoger.chat_service.chat.domain;

import com.yoger.chat_service.common.entity.BaseEntity;
import com.yoger.chat_service.message.domain.MessageEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_table")
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String title;

    private String lastMessage;

    private Integer unreadCount;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private List<MessageEntity> messageEntities;

    private String thumbnailUrl;


    public ChatEntity(LocalDateTime createTime, LocalDateTime modifiedTime,
                      LocalDateTime deletedTime, State state, Long id, String title, String lastMessage,
                      Integer unreadCount, List<MessageEntity> messageEntities, String thumbnailUrl) {
        super(createTime, modifiedTime, deletedTime, state);
        this.id = id;
        this.title = title;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.messageEntities = messageEntities;
        this.thumbnailUrl = thumbnailUrl;
    }
}
