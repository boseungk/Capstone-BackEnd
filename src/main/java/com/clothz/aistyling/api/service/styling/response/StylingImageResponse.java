package com.clothz.aistyling.api.service.styling.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "추천 이미지 response")
public record StylingImageResponse(@Schema(description = "추천 이미지들") List<String> images) {
    @Builder
    public StylingImageResponse(List<String> images) {
        this.images = images;
    }
    public static StylingImageResponse from(List<String> images) {
        return StylingImageResponse.builder()
                .images(images)
                .build();
    }
}
