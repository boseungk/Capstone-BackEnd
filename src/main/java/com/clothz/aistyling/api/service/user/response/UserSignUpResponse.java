package com.clothz.aistyling.api.service.user.response;

import com.clothz.aistyling.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "유저 회원가입 response")
public record UserSignUpResponse(@Schema(description = "유저 가입 이메일") String email,
                                 @Schema(description = "유저 가입 닉네임") String nickname) {
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
