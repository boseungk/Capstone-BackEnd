package com.clothz.aistyling.api.service.user.response;


import com.clothz.aistyling.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "유저 변경 response")
public record UserUpdateResponse(@Schema(description = "유저 변경 이메일") String email,
                                 @Schema(description = "유저 변경 닉네임") String nickname,
                                 @Schema(description = "유저 변경 패스워드") String password) {

    @Builder
    public UserUpdateResponse(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public static UserUpdateResponse from(User user){
        return UserUpdateResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .build();
    }
}


