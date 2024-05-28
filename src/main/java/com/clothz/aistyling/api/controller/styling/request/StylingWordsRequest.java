package com.clothz.aistyling.api.controller.styling.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "이미지 클릭시 전송 request")
public record StylingWordsRequest(@Schema(description = "이미지를 클릭하면 전송되는 단어들") String inputs) {
    @Builder
    public StylingWordsRequest(String inputs) {
        this.inputs = inputs;
    }

}
