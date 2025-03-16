package com.yoger.chat_service.consumer.userinfo.service;

import com.mog.authserver.auth.event.UserUpsertEvent;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.mapper.UserInfoEntityMapper;
import com.yoger.chat_service.userInfo.service.UserInfoPersistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUpsertService {

    private final UserInfoPersistService userInfoPersistService;

    public void saveUserInfo(UserUpsertEvent event) {
        try {
            UserInfoEntity userInfoEntity = userInfoPersistService.findById(event.id());
            UserInfoEntity modifiedUserInfo = UserInfoEntityMapper.modifyUser(userInfoEntity, event);
            userInfoPersistService.save(modifiedUserInfo);

        } catch (RuntimeException ex) {
            UserInfoEntity createdUserInfo = UserInfoEntityMapper.createUser(event);
            userInfoPersistService.save(createdUserInfo);
        }
    }
}
