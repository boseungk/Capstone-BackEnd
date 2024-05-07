package com.clothz.aistyling.api.service.home.response;

import com.clothz.aistyling.domain.home.Home;
import lombok.Builder;

public record HomeResponse(String image, String sentence) {

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
