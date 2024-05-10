package com.clothz.aistyling.api.service.user.response;

import com.clothz.aistyling.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "유저 정보 response")
public record UserInfoResponse(@Schema(description = "유저 이메일") String email,
                               @Schema(description = "유저 닉네임") String nickname,
                               @Schema(description = "유저 이미지들") List<String> images) {
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
