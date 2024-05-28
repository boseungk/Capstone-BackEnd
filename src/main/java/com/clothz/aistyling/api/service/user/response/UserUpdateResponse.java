package com.clothz.aistyling.api.service.user.response;


import com.clothz.aistyling.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "유저 변경 response")
public record UserUpdateResponse(@Schema(description = "유저 변경 이메일") String email,
                                 @Schema(description = "유저 변경 닉네임") String nickname) {

    @Builder
    public UserUpdateResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static UserUpdateResponse from(User user){
        return UserUpdateResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}


