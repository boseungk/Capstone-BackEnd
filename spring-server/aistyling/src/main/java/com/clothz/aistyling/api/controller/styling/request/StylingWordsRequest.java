package com.clothz.aistyling.api.controller.styling.request;

import lombok.Builder;

public record StylingWordsRequest(String words) {
    @Builder
    public StylingWordsRequest(String words) {
        this.words = words;
    }

}
