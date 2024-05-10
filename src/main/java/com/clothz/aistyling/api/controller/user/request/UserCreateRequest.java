package com.clothz.aistyling.api.controller.user.request;

import com.clothz.aistyling.domain.user.User;
import com.clothz.aistyling.domain.user.constant.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "User 회원가입")
public record UserCreateRequest(
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        @Schema(description = "이메일")
        String email,
        @NotEmpty
        @Size(min = 3, max = 20, message = "3에서 20자 이내여야 합니다.")
        @Schema(description = "닉네임", minLength = 3, maxLength = 20)
        String nickname,
        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        @Schema(description = "비밀번호", minLength = 8, maxLength = 20)
        String password
) {

    @Builder
    public UserCreateRequest(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
