package com.grocio.backend.authentication.mapper;

import com.grocio.backend.authentication.dto.response.UserSummary;
import com.grocio.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public UserSummary toUserSummary(User user) {
        if (user == null) {
            return null;
        }

        return UserSummary.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .profileImage(user.getProfileImageUrl())
                .walletBalance(user.getWalletBalance())
                .build();
    }
}
