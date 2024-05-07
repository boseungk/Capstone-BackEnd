package com.clothz.aistyling.api.service.user.response;


import com.clothz.aistyling.domain.user.User;
import lombok.Builder;
public record UserUpdateResponse(String email, String nickname, String password) {

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


