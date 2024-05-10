package com.clothz.aistyling.api.service.styling.response;

import com.clothz.aistyling.domain.styling.Styling;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "스타일 예시")
public record StylingExampleResponse(@Schema(description = "좋은 생성형 이미지 예시") String image,
                                     @Schema(description = "좋은 프롬프트 예시") String prompt) {
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
