package com.yoger.chat_service.userInfo.service;

import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoQueryService {
    private final UserInfoPersistService userInfoPersistService;

    @Transactional
    public List<UserInfoEntity> findAllByIds(List<Long> ids) {
        return userInfoPersistService.findAllById(ids);
    }

    @Transactional
    public UserInfoEntity findById(Long id) {
        return userInfoPersistService.findById(id);
    }

}
