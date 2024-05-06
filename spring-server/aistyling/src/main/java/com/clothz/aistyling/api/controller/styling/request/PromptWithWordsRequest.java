package com.clothz.aistyling.api.controller.styling.request;

import lombok.Builder;

import java.util.List;

public record PromptWithWordsRequest(String inputs, List<String> inputIdImages) {

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
