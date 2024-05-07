package com.clothz.aistyling.api.service.user.response;

import lombok.Builder;

import java.util.List;

public record UserImagesResponse(List<String> imgUrls) {
    @Builder
    public UserImagesResponse(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public static UserImagesResponse from(List<String> imgUrls) {
        return UserImagesResponse.builder()
                .imgUrls(imgUrls)
                .build();
    }
}
