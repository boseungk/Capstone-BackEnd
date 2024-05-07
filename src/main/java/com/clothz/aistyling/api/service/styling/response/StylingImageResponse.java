package com.clothz.aistyling.api.service.styling.response;

import lombok.Builder;

import java.util.List;

public record StylingImageResponse(List<String> images) {
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
