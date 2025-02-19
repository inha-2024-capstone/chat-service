package com.yoger.chat_service.userInfo.service;

import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.repository.UserInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoPersistService {
    private final UserInfoRepository userInfoRepository;

    public UserInfoEntity findById(Long id) {
        return userInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 기업 정보가 존재하지 않습니다."));
    }

    public List<UserInfoEntity> findAllById(List<Long> ids) {
        return userInfoRepository.findAllById(ids);
    }

    public UserInfoEntity save(UserInfoEntity comInfo) {
        return userInfoRepository.save(comInfo);
    }
}
