package com.clothz.aistyling.api.controller.styling;

import com.clothz.aistyling.api.ApiResponse;
import com.clothz.aistyling.api.controller.styling.request.StylingWordsRequest;
import com.clothz.aistyling.api.service.styling.StylingService;
import com.clothz.aistyling.api.service.styling.response.StylingExampleResponse;
import com.clothz.aistyling.api.service.styling.response.StylingImageResponse;
import com.clothz.aistyling.global.jwt.userInfo.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class StylingController {
    private static final String AI_URL_WITH_WORDS = "/api/styling/words";
    private static final String AI_URL_WITH_SENTENCES = "/api/styling/sentences";
    private final StylingService stylingService;

    @GetMapping("/styling")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<List<StylingExampleResponse>> getUserInfo(
            @AuthenticationPrincipal final CustomUserDetails userDetails){
        final var imageAndPrompt = stylingService.getImageAndPrompt();
        return ApiResponse.ok(imageAndPrompt);
    }

    @PostMapping("/styling/words")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<StylingImageResponse> getImageWithWords(
            @RequestBody @Valid final StylingWordsRequest request,
            @AuthenticationPrincipal final CustomUserDetails userDetails) throws JsonProcessingException {
        final var imageWithWords = stylingService.getImageWithWords(AI_URL_WITH_WORDS, request, userDetails.getId());
        return ApiResponse.ok(imageWithWords);
    }
}
