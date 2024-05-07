package com.clothz.aistyling.api.service.user.response;

import com.clothz.aistyling.domain.user.User;
import lombok.Builder;

import java.util.List;

public record UserSingUpResponse(String email, String nickname) {
    @Builder
    public UserSingUpResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static UserSingUpResponse from(User user) {
        return UserSingUpResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
