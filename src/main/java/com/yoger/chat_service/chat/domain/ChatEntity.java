package com.yoger.chat_service.chat.domain;

import com.yoger.chat_service.common.converter.ListToJsonConverter;
import com.yoger.chat_service.common.entity.BaseEntity;
import com.yoger.chat_service.message.domain.MessageEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "user_ids", columnDefinition = "JSON")
    @Convert(converter = ListToJsonConverter.class)
    private List<Long> userIds;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_id")
    @OrderBy("createTime ASC")
    private List<MessageEntity> messageEntities;

    public ChatEntity(LocalDateTime createTime, LocalDateTime modifiedTime, Long id,
                      List<Long> userIds, List<MessageEntity> messageEntities) {
        super(createTime, modifiedTime);
        this.id = id;
        this.userIds = userIds;
        this.messageEntities = messageEntities;
    }
}
