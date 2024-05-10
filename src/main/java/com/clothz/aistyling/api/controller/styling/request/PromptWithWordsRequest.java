package com.clothz.aistyling.api.controller.styling.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "프롬프트와 이미지들 request")
public record PromptWithWordsRequest(@Schema(description = "프롬프트") String inputs,
                                     @Schema(description = "이미지들") List<String> inputIdImages) {

    @Builder
    public PromptWithWordsRequest(final String inputs, final List<String> inputIdImages){
        this.inputs = inputs;
        this.inputIdImages = inputIdImages;
    }

    public static PromptWithWordsRequest of(final String inputs, final List<String> inputIdImages){
        return PromptWithWordsRequest.builder()
                .inputs(inputs)
                .inputIdImages(inputIdImages)
                .build();
    }
}
