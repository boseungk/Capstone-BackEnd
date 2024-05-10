package com.clothz.aistyling.api.service.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "이미지 url response")
public record UserImagesResponse(@Schema(description = "이미지 url들") List<String> imgUrls) {
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
