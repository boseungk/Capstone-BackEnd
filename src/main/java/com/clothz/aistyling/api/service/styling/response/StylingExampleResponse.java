package com.clothz.aistyling.api.service.styling.response;

import com.clothz.aistyling.domain.styling.Styling;
import lombok.Builder;

public record StylingExampleResponse(String image, String prompt) {
    @Builder
    public StylingExampleResponse(String image, String prompt) {
        this.image = image;
        this.prompt = prompt;
    }
    public static StylingExampleResponse from(Styling styling) {
        return StylingExampleResponse.builder()
                .image(styling.getImage())
                .prompt(styling.getPrompt())
                .build();
    }
}
