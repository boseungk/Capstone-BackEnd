package com.clothz.aistyling.api.service.user.response;

import com.clothz.aistyling.domain.user.User;
import lombok.Builder;

public record UserSignUpResponse(String email, String nickname) {
    @Builder
    public UserSignUpResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static UserSignUpResponse from(User user) {
        return UserSignUpResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
