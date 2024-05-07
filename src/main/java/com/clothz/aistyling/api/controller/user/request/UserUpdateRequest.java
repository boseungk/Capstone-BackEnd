package com.clothz.aistyling.api.controller.user.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record UserUpdateRequest(
        @NotEmpty
        @Size(min = 3, max = 20, message = "3에서 20자 이내여야 합니다.")
        String nickname,
        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        String password
) {

    @Builder
    public UserUpdateRequest(final String nickname, final String password) {
        this.nickname = nickname;
        this.password = password;
    }
}

