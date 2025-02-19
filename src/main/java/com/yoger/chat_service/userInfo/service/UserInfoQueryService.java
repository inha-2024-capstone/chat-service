package com.yoger.chat_service.userInfo.service;

import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class UserInfoQueryService {
    private final UserInfoPersistService userInfoPersistService;

    @Transactional(readOnly = true)
    public List<UserInfoEntity> findAllByIds(List<Long> ids) {
        return userInfoPersistService.findAllById(ids);
    }

    @Transactional(readOnly = true)
    public UserInfoEntity findById(Long id) {
        return userInfoPersistService.findById(id);
    }

}
