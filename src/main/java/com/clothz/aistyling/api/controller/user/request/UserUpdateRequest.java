package com.clothz.aistyling.api.controller.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Schema(description = "User 회원정보 업데이트")
public record UserUpdateRequest(
        @NotBlank
        @Size(min = 3, max = 20, message = "3에서 20자 이내여야 합니다.")
        @Schema(description = "닉네임", minLength = 3, maxLength = 20)
        String nickname,
        @NotBlank
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        @Schema(description = "비밀번호", minLength = 8, maxLength = 20)
        String password
) {

    @Builder
    public UserUpdateRequest(final String nickname, final String password) {
        this.nickname = nickname;
        this.password = password;
    }
}

