package com.clothz.aistyling.api.service.user.response;

import com.clothz.aistyling.domain.user.User;
import lombok.Builder;

import java.util.List;

public record UserInfoResponse(String email, String nickname, List<String> images) {
    @Builder
    public UserInfoResponse(String email, String nickname, List<String> images) {
        this.email = email;
        this.nickname = nickname;
        this.images = images;
    }
    public static UserInfoResponse of(User user, List<String> imgUrls) {
        return UserInfoResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .images(imgUrls)
                .build();
    }
}
