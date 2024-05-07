package com.clothz.aistyling.global.jwt.dto;

import lombok.Builder;

public record LoginResponse(String nickname) {
    @Builder
    public LoginResponse(String nickname) {
        this.nickname = nickname;
    }
    public static LoginResponse from(String nickname) {
        return LoginResponse.builder()
                .nickname(nickname)
                .build();
    }}
