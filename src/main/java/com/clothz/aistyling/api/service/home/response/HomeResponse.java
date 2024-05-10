package com.clothz.aistyling.api.service.home.response;

import com.clothz.aistyling.domain.home.Home;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "서비스 소개")
public record HomeResponse(@Schema(description = "서비스 소개 이미지") String image,
                           @Schema(description = "서비스 소개 글") String sentence) {

    @Builder
    public HomeResponse(String image, String sentence) {
        this.image = image;
        this.sentence = sentence;
    }

    public static HomeResponse from(Home home) {
        return HomeResponse.builder()
                .image(home.getImage())
                .sentence(home.getSentence())
                .build();
    }
}
