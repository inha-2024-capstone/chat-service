package com.yoger.chat_service.userInfo.mapper;

import com.mog.authserver.auth.event.UserUpsertEvent;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;

public class UserInfoEntityMapper {

    public static UserInfoEntity createUser(UserUpsertEvent event) {
        return new UserInfoEntity(
                event.id(),
                event.username(),
                event.email(),
                event.imageUrl(),
                event.role()
        );

    }

    public static UserInfoEntity modifyUser(UserInfoEntity userInfoEntity, UserUpsertEvent event) {
        return new UserInfoEntity(
                userInfoEntity.getCreateTime(),
                userInfoEntity.getModifiedTime(),
                event.id(),
                event.username(),
                event.email(),
                event.imageUrl(),
                event.role()
        );
    }
}
