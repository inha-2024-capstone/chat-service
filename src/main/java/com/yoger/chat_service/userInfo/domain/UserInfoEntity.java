package com.yoger.chat_service.userInfo.domain;

import com.yoger.chat_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_user_table")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEntity extends BaseEntity {

    @Id
    @Column(name = "auth_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    Role role;

    public UserInfoEntity(LocalDateTime createTime, LocalDateTime modifiedTime, Long id,
                          String userName, String email, String imageUrl, Role role) {
        super(createTime, modifiedTime);
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }
}