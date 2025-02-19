package com.yoger.chat_service.userInfo.domain;

import com.yoger.chat_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
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
}