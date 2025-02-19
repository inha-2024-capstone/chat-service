package com.yoger.chat_service.userInfo.mapper;

import com.yoger.chat_service.userInfo.domain.Role;
import com.yoger.chat_service.userInfo.domain.UserInfoEntity;
import com.yoger.chat_service.userInfo.dto.response.CompanyPassDTO;
import com.yoger.chat_service.userInfo.dto.response.UserPassDTO;

public class UserInfoMapper {

    public static UserInfoEntity createUserInfoEntity(UserPassDTO userPassDTO, Long id) {
        return new UserInfoEntity(
                id,
                userPassDTO.username(),
                userPassDTO.email(),
                userPassDTO.imageUri(),
                Role.USER
        );
    }

    public static UserInfoEntity createUserInfoEntity(CompanyPassDTO companyPassDTO, Long id) {
        return new UserInfoEntity(
                id,
                companyPassDTO.companyName(),
                companyPassDTO.email(),
                companyPassDTO.imageUrl(),
                Role.COMPANY
        );
    }
}
